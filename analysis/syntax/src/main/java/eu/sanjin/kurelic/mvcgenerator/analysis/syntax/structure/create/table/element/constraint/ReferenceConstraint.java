package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Tokens;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.reference.ReferenceAction;

public class ReferenceConstraint implements Constraint {

    private Token tableName;
    private Tokens columnList;
    private ReferenceAction updateAction;
    private ReferenceAction deleteAction;

    public Token getTableName() {
        return tableName;
    }

    public void setTableName(Token tableName) {
        this.tableName = tableName;
    }

    public Tokens getColumnList() {
        return columnList;
    }

    public void setColumnList(Tokens columnList) {
        this.columnList = columnList;
    }

    public ReferenceAction getUpdateAction() {
        return updateAction;
    }

    public void setUpdateAction(ReferenceAction updateAction) {
        this.updateAction = updateAction;
    }

    public ReferenceAction getDeleteAction() {
        return deleteAction;
    }

    public void setDeleteAction(ReferenceAction deleteAction) {
        this.deleteAction = deleteAction;
    }
}
