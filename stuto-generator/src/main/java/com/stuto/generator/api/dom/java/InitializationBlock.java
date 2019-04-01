package com.stuto.generator.api.dom.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import com.stuto.generator.util.OutputUtilities;

/**
* 类说明
* @author 作者 : zyq
* 创建时间：2017年3月6日 下午4:48:11
* @version
*/
public class InitializationBlock {

    private boolean isStatic;
    private List<String> bodyLines;
    private List<String> javaDocLines;

    public InitializationBlock() {
        this(false);
    }

    public InitializationBlock(boolean isStatic) {
        this.isStatic = isStatic;
        bodyLines = new ArrayList<String>();
        javaDocLines = new ArrayList<String>();
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public List<String> getBodyLines() {
        return bodyLines;
    }

    public void addBodyLine(String line) {
        bodyLines.add(line);
    }

    public void addBodyLine(int index, String line) {
        bodyLines.add(index, line);
    }

    public void addBodyLines(Collection<String> lines) {
        bodyLines.addAll(lines);
    }

    public void addBodyLines(int index, Collection<String> lines) {
        bodyLines.addAll(index, lines);
    }

    public List<String> getJavaDocLines() {
        return javaDocLines;
    }

    public void addJavaDocLine(String javaDocLine) {
        javaDocLines.add(javaDocLine);
    }

    public String getFormattedContent(int indentLevel) {
        StringBuilder sb = new StringBuilder();

        for (String javaDocLine : javaDocLines) {
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append(javaDocLine);
            OutputUtilities.newLine(sb);
        }

        OutputUtilities.javaIndent(sb, indentLevel);

        if (isStatic) {
            sb.append("static "); //$NON-NLS-1$
        }

        sb.append('{');
        indentLevel++;

        ListIterator<String> listIter = bodyLines.listIterator();
        while (listIter.hasNext()) {
            String line = listIter.next();
            if (line.startsWith("}")) { //$NON-NLS-1$
                indentLevel--;
            }

            OutputUtilities.newLine(sb);
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append(line);

            if ((line.endsWith("{") && !line.startsWith("switch")) //$NON-NLS-1$ //$NON-NLS-2$
                    || line.endsWith(":")) { //$NON-NLS-1$
                indentLevel++;
            }

            if (line.startsWith("break")) { //$NON-NLS-1$
                // if the next line is '}', then don't outdent
                if (listIter.hasNext()) {
                    String nextLine = listIter.next();
                    if (nextLine.startsWith("}")) { //$NON-NLS-1$
                        indentLevel++;
                    }

                    // set back to the previous element
                    listIter.previous();
                }
                indentLevel--;
            }
        }

        indentLevel--;
        OutputUtilities.newLine(sb);
        OutputUtilities.javaIndent(sb, indentLevel);
        sb.append('}');

        return sb.toString();
    }
}
