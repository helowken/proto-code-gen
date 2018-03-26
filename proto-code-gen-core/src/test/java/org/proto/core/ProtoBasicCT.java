package org.proto.core;

import org.junit.Assert;
import org.junit.Test;
import org.proto.serdes.ProtoUtils;
import org.proto.serdes.annotation.ProtoClass;
import org.proto.serdes.annotation.ProtoField;
import org.proto.serdes.error.DuplicatedFieldIndexException;
import org.proto.serdes.error.DuplicatedFieldNameException;
import org.proto.serdes.error.InvalidTypeClassException;
import org.proto.serdes.error.NoArgTypeClassFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;

public class ProtoBasicCT {
    private static final Logger logger = LoggerFactory.getLogger(ProtoBasicCT.class);

    @ProtoClass(protoClass = "Fail")
    private static class DuplicatedFieldIndex {
        @ProtoField(index = 1)
        public String a;
        @ProtoField(index = 1)
        public String b;
    }

    @ProtoClass(protoClass = "Fail")
    private static class DuplicateFieldName {
        @ProtoField(index = 1, field = "a")
        public String a;
        @ProtoField(index = 2, field = "a")
        public String b;
    }

    @Test
    public void testDuplicatedFieldIndex() throws Throwable {
        try {
            ProtoUtils.getProtoFieldPairs(DuplicatedFieldIndex.class);
            fail();
        } catch (DuplicatedFieldIndexException t) {
            logger.error(t.getMessage());
        }
    }

    @Test
    public void testDuplicatedFieldName() throws Throwable {
        try {
            ProtoUtils.getProtoFieldPairs(DuplicateFieldName.class);
            Assert.fail();
        } catch (DuplicatedFieldNameException t) {
            logger.error(t.getMessage());
        }
    }

    @ProtoClass(protoClass = "A")
    private static class NoArgTypeList {
        @ProtoField(index = 1)
        private List f;

        public List getF() {
            return f;
        }

        public void setF(List f) {
            this.f = f;
        }
    }

    @ProtoClass(protoClass = "A")
    private static class NoArgTypeMap {
        @ProtoField(index = 1)
        public Map f;
    }

    @ProtoClass(protoClass = "A")
    private static class InvalidArgTypeField {
        @ProtoField(index = 1)
        public Object f;
    }

    @ProtoClass(protoClass = "A")
    private static class InvalidArgTypeList {
        @ProtoField(index = 1)
        public List<Object> f;
    }

    @Test
    public void testInvalidArgType() {
        try {
            ProtoUtils.getProtoFieldPairs(NoArgTypeList.class);
            Assert.fail();
        } catch (NoArgTypeClassFoundException t) {
            logger.error(t.getMessage());
        }

        try {
            ProtoUtils.getProtoFieldPairs(NoArgTypeMap.class);
            Assert.fail();
        } catch (NoArgTypeClassFoundException t) {
            logger.error(t.getMessage());
        }

        try {
            ProtoUtils.getProtoFieldPairs(InvalidArgTypeField.class);
            Assert.fail();
        } catch (InvalidTypeClassException t) {
            logger.error(t.getMessage());
        }

        try {
            ProtoUtils.getProtoFieldPairs(InvalidArgTypeList.class);
            Assert.fail();
        } catch (InvalidTypeClassException t) {
            logger.error(t.getMessage());
        }
    }
}
