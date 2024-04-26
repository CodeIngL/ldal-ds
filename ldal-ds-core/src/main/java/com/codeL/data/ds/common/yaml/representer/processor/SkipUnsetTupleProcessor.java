/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codeL.data.ds.common.yaml.representer.processor;

import org.yaml.snakeyaml.nodes.*;

/**
 * Skip unset tuple processor. 
 */
public final class SkipUnsetTupleProcessor implements TupleProcessor {
    
    @Override
    public String getProcessedTupleName() {
        return null;
    }
    
    @Override
    public NodeTuple process(final NodeTuple nodeTuple) {
        return isUnsetNodeTuple(nodeTuple.getValueNode()) ? null : nodeTuple;
    }
    
    private boolean isUnsetNodeTuple(final Node valueNode) {
        return isNullNode(valueNode) || isEmptyCollectionNode(valueNode);
    }
    
    private boolean isNullNode(final Node valueNode) {
        return Tag.NULL.equals(valueNode.getTag());
    }
    
    private boolean isEmptyCollectionNode(final Node valueNode) {
        return valueNode instanceof CollectionNode && (isEmptySequenceNode(valueNode) || isEmptyMappingNode(valueNode));
    }
    
    private boolean isEmptySequenceNode(final Node valueNode) {
        return Tag.SEQ.equals(valueNode.getTag()) && ((SequenceNode) valueNode).getValue().isEmpty();
    }
    
    private boolean isEmptyMappingNode(final Node valueNode) {
        return Tag.MAP.equals(valueNode.getTag()) && ((MappingNode) valueNode).getValue().isEmpty();
    }
}