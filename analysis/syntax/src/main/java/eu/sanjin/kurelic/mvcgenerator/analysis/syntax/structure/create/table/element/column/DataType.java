package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.column;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.utility.XmlTagBuilder;

public class DataType {

    private static final String TYPE_TAG = "type";
    private static final String PRECISION_OR_LENGTH_TAG = "precision-length";
    private static final String SCALE_TAG = "scale";

    private Token type;
    private Token precisionOrLength;
    private Token scale;

    public DataType(Token type) {
        this.type = type;
    }

    public Token getType() {
        return type;
    }

    public Token getPrecisionOrLength() {
        return precisionOrLength;
    }

    public void setPrecisionOrLength(Token precisionOrLength) {
        this.precisionOrLength = precisionOrLength;
    }

    public Token getScale() {
        return scale;
    }

    public void setScale(Token scale) {
        this.scale = scale;
    }

    @Override
    public String toString() {
        return XmlTagBuilder.getStartTag(this) +
                XmlTagBuilder.suroundToken(type, TYPE_TAG) +
                XmlTagBuilder.suroundToken(precisionOrLength, PRECISION_OR_LENGTH_TAG) +
                XmlTagBuilder.suroundToken(scale, SCALE_TAG) +
                XmlTagBuilder.getEndTag(this);
    }

}
