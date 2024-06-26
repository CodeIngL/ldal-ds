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

package com.codeL.data.ds.atom.yaml.constructor;

import com.codeL.data.ds.atom.yaml.YamlNoneSingleConfig;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.nodes.Node;

/**
 * YAML none sharding strategy configuration construct.
 */
public final class YamlNoneSingleConfigConstruct implements Construct {

    @Override
    public Object construct(final Node node) {
        return new YamlNoneSingleConfig();
    }

    @Override
    public void construct2ndStep(final Node node, final Object newInstance) {
    }
}
