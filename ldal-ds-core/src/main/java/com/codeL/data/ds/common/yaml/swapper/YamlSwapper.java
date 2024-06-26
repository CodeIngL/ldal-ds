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

package com.codeL.data.ds.common.yaml.swapper;


import com.codeL.data.ds.common.yaml.config.YamlConfiguration;

/**
 * YAML configuration swapper.
 *
 * @param <Y> type of YAML configuration
 * @param <T> type of swapped object
 */
public interface YamlSwapper<Y extends YamlConfiguration, T> {
    
    /**
     * Swap to YAML configuration.
     *
     * @param data data to be swapped
     * @return YAML configuration
     */
    Y swap(T data);
    
    /**
     * Swap from YAML configuration to object.
     *
     * @param yamlConfiguration YAML configuration
     * @return swapped object
     */
    T swap(Y yamlConfiguration);
}
