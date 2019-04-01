package com.stuto.generator.internal;

import com.stuto.core.pub.StringUtil;
import com.stuto.generator.api.IntrospectedColumn;
import com.stuto.generator.api.IntrospectedForeignKey;
import com.stuto.generator.api.IntrospectedPrimaryKey;
import com.stuto.generator.api.IntrospectedTable;
import com.stuto.generator.config.GeneratorContext;
import com.stuto.generator.config.TableChooseConfiguration;
import com.stuto.generator.config.TableConfig;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;

/**
* 数据库信息封装类
* @author 作者 : zyq
* 创建时间：2016年12月17日 下午5:38:12
* @version
*/
@Slf4j
public class DatabaseIntrospector {

	private DatabaseMetaData databaseMetaData;
	private Connection conn;
    private List<String> warnings;

    /**
     * 讲数据库信息封装成实体类
     * @param tc	表选择配置
     * @return
     * @throws SQLException
     */
    public List<IntrospectedTable> introspectTables(TableChooseConfiguration tc) throws SQLException{
        Set<String> chooseTables= tc.getChooseTables();
    	String remark="";
    	//如果被选择的表不为空
    	if(chooseTables!=null&&chooseTables.size()>0){
    		Iterator<String> iterator=chooseTables.iterator();
    		String tableName="";
    		while (iterator.hasNext()) {
    			tableName=iterator.next();
    			ResultSet resultSet=databaseMetaData.getTables(tc.getCatalog(), tc.getSchema(), tableName, new String[]{"table"});
                return loadTables(resultSet,tc);
			}
    	}else{
    		//没有指定操作哪些表，用模糊查询匹配表
    		String tableLike=tc.getNameLike();
    		if(tableLike==null||tableLike.trim().length()==0){tableLike="%";}
    		ResultSet resultSet=databaseMetaData.getTables(tc.getCatalog(), tc.getSchema(), tableLike, new String[]{"table"});
            return loadTables(resultSet,tc);
    	}
    	return null;
    }

    private List<IntrospectedTable> loadTables(ResultSet resultSet,TableChooseConfiguration tc) throws SQLException {
        List<IntrospectedTable> introspectedTables=new ArrayList<>();
        IntrospectedTable table=null;
        //当前操作的第几个表
        int current=0;
        String tableName="";
        while (resultSet.next()) {
            tableName=resultSet.getString("TABLE_NAME");
            tableName=tableName.toUpperCase();
            current++;
            log.debug("正在读取第   "+current+"   个表，表名："+tableName);
            if(tc.getIgnoredTables()!=null&&tc.getIgnoredTables().contains(tableName)){
                warnings.add(tableName+"===被忽略");
                continue;
            }
            table=new IntrospectedTable();
            table.init(resultSet.getString("TABLE_CAT"), resultSet.getString("TABLE_SCHEM"),tableName );
            //赋值注释
            table.setRemarks(resultSet.getString("REMARKS"));
            //赋值外键
            fillForeignKey(table);
            //赋值主键
            fillPrimaryKey(table);
            fillColumns( table,tc );
            //如果注释没有查询到
            if(StringUtil.isEmpty(table.getRemarks())){
                table.setRemarks(getTableRemark(tableName));
            }

            // 设置自定义表配置
            TableConfig tableConfig = new TableConfig();
            tableConfig.setModule(GeneratorContext.cfgJson.getJSONObject("individual").getString("defaultModule"));
            table.setTableConfig(tableConfig);

            introspectedTables.add(table);
        }
        return introspectedTables;
    }


    //处理databasemeta获取不到表注释的情况
    private String getTableRemark(String tableName) throws SQLException{
    	if(conn==null){
    		conn=databaseMetaData.getConnection();
    	}
    	String sql="show create table "+tableName;
    	PreparedStatement ps=conn.prepareStatement(sql);
    	ResultSet rs=ps.executeQuery();
    	String remark="";
    	while (rs.next()) {
    		String cc=rs.getString(2);
    		int index=cc.lastIndexOf('=');
    		if(cc.substring(index-7).toLowerCase().startsWith("comment")){
    			remark=cc.substring(index+2,cc.length()-1);
    		}
		}

    	return remark;
    }
    //获取表的外键信息
    public void fillForeignKey(IntrospectedTable t) throws SQLException{
    	ResultSet resultSet=databaseMetaData.getImportedKeys(t.getTableCatalog(), t.getTableSchema(), t.getTableName());
    	Set<IntrospectedForeignKey> fkSet=new HashSet<IntrospectedForeignKey>();
    	IntrospectedForeignKey fk=null;
    	while (resultSet.next()) {
    		fk=new IntrospectedForeignKey();
    		fk.setDeferrAbility(resultSet.getShort("DEFERRABILITY"));
    		fk.setDeleteRule(resultSet.getShort("DELETE_RULE"));
    		fk.setFkColumnName(resultSet.getString("FKCOLUMN_NAME"));
    		fk.setFkName(resultSet.getString("FK_NAME"));
    		fk.setFkTableCatalog(resultSet.getString("FKTABLE_CAT"));
    		fk.setFkTableName(resultSet.getString("FKTABLE_NAME"));
    		fk.setFkTableSchema(resultSet.getString("FKTABLE_SCHEM"));
    		fk.setKeySeq(resultSet.getShort("KEY_SEQ"));
    		fk.setPkColumnName(resultSet.getString("PKCOLUMN_NAME"));
    		fk.setPkTableCatalog(resultSet.getString("PKTABLE_CAT"));
    		fk.setPkTableName(resultSet.getString("PKTABLE_NAME"));
    		fk.setPkTableSchema(resultSet.getString("PKTABLE_SCHEM"));
    		fk.setUpdateRule(resultSet.getShort("UPDATE_RULE"));

    		fkSet.add(fk);
		}
    	t.setForeignKeys(fkSet);
    }

  //获取表的主键信息
    public void fillPrimaryKey(IntrospectedTable t) throws SQLException{
    	ResultSet resultSet=databaseMetaData.getPrimaryKeys(t.getTableCatalog(), t.getTableSchema(), t.getTableName());
    	Set<IntrospectedPrimaryKey> pkSet=new HashSet<IntrospectedPrimaryKey>();
    	IntrospectedPrimaryKey pk=null;
    	while (resultSet.next()) {
    		pk=new IntrospectedPrimaryKey();
    		pk.setColumnName(resultSet.getString("COLUMN_NAME"));
    		pk.setKeySeq(resultSet.getShort("KEY_SEQ"));
    		pk.setPrimaryKeyName(resultSet.getString("PK_NAME"));
    		pk.setTableCatalog(resultSet.getString("TABLE_CAT"));
    		pk.setTableName(resultSet.getString("TABLE_NAME"));
    		pk.setTableSchema(resultSet.getString("TABLE_SCHEM"));
    		pkSet.add(pk);
		}
    	t.setPrimaryKeys(pkSet);
    }

    //获取表的列信息
    public void fillColumns(IntrospectedTable t,TableChooseConfiguration tc) throws SQLException{
    	ResultSet resultSet=databaseMetaData.getColumns(t.getTableCatalog(), t.getTableSchema(), t.getTableName(),"%");
    	List<IntrospectedColumn> columns=new ArrayList<IntrospectedColumn>();
    	IntrospectedColumn column=null;
    	Set<String> ignoreColumns=tc.getIgnoredColumns();

    	Map<String,IntrospectedPrimaryKey> pkMap=new HashMap<String, IntrospectedPrimaryKey>();
		Map<String,IntrospectedForeignKey> fkMap=new HashMap<String, IntrospectedForeignKey>();
		IntrospectedPrimaryKey pk=null;
		IntrospectedForeignKey fk=null;
		Iterator<IntrospectedPrimaryKey> iterator=t.getPrimaryKeys().iterator();
		while (iterator.hasNext()) {
			pk=iterator.next();
			pkMap.put(pk.getColumnName().toLowerCase(), pk);
		}
		Iterator<IntrospectedForeignKey> fkiterator=t.getForeignKeys().iterator();
		while (fkiterator.hasNext()) {
			fk=fkiterator.next();
			fkMap.put(fk.getFkColumnName().toLowerCase(), fk);
		}
		String columnName="";
    	while (resultSet.next()) {
    		columnName=resultSet.getString("COLUMN_NAME").toLowerCase();

    		//如果该列被忽略
    		if(ignoreColumns!=null&&ignoreColumns.contains(columnName))continue;

    		column=new IntrospectedColumn();
    		column.setColumnName(columnName);
    		column.setColumnSize(resultSet.getInt("COLUMN_SIZE"));
    		column.setDataType(resultSet.getInt("DATA_TYPE"));
    		column.setDecimalDigits(resultSet.getInt("DECIMAL_DIGITS"));
    		column.setIsAutoIncrement(resultSet.getString("IS_AUTOINCREMENT"));
    		column.setNullable(resultSet.getInt("NULLABLE"));
    		column.setRemarks(resultSet.getString("REMARKS"));
    		column.setTableCatalog(resultSet.getString("TABLE_CAT"));
    		column.setTableName(resultSet.getString("TABLE_NAME"));
    		column.setTableSchema(resultSet.getString("TABLE_SCHEM"));
    		column.setTypeName(resultSet.getString("TYPE_NAME"));
    		column.setDefaultValue(resultSet.getString("COLUMN_DEF"));

    		if(pkMap.containsKey(columnName)){
    			column.setPrimaryKey(true);
    			if(t.getFirstPrimaryKeyColumn()==null){
    			    t.setFirstPrimaryKeyColumn(column);
                }
    		}
    		if(fkMap.containsKey(columnName)){
    			column.setForeignKey(fkMap.get(columnName).getPkTableName()+"."+fkMap.get(columnName).getPkColumnName());
    		}
    		columns.add(column);
		}
    	t.setColumns(columns);
    }

	public void setDatabaseMetaData(DatabaseMetaData databaseMetaData) {
		this.databaseMetaData = databaseMetaData;
	}

    public static void main(String[] args) {
		Set<String> set=new HashSet<String>();
		set.add("hello");
		System.out.println("hello"==new String("hello"));
		boolean d=set.contains(new String("hello"));
		System.out.println(d);
	}

}
