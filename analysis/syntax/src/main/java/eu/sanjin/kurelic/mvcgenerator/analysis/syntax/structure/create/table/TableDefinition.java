package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.TableElementList;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.utility.XmlTagBuilder;

import java.io.Serializable;

public class TableDefinition implements Serializable {

    private static final String TABLE_NAME_TAG = "TableName";
    private Token tableName;
    private TableElementList tableElementList;

    public Token getTableName() {
        return tableName;
    }

    public void setTableName(Token tableName) {
        this.tableName = tableName;
    }

    public TableElementList getTableElementList() {
        return tableElementList;
    }

    public void setTableElementList(TableElementList tableElementList) {
        this.tableElementList = tableElementList;
    }

    @Override
    public String toString() {
        return XmlTagBuilder.getStartTag(this) +
                XmlTagBuilder.suroundToken(tableName, TABLE_NAME_TAG) +
                tableElementList +
                XmlTagBuilder.getEndTag(this);
    }

}
