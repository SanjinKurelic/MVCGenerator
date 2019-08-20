package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create;

import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.TableDefinition;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.utility.XmlTagBuilder;

import java.io.Serializable;

public class CreateTableDefinition implements Serializable, CreateDefinition {

    private TableDefinition tableDefinition;

    public CreateTableDefinition(TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    @Override
    public String toString() {
        return XmlTagBuilder.getStartTag(this) +
                tableDefinition +
                XmlTagBuilder.getEndTag(this);
    }

}
