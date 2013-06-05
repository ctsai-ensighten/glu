/*
 * Copyright (c) 2013 Yan Pujante
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.pongasoft.glu.provisioner.core.metamodel.impl

import org.pongasoft.glu.provisioner.core.metamodel.AgentMetaModel
import org.pongasoft.glu.provisioner.core.metamodel.ConsoleMetaModel
import org.pongasoft.glu.provisioner.core.metamodel.FabricMetaModel
import org.pongasoft.glu.provisioner.core.metamodel.GluMetaModel
import org.pongasoft.glu.provisioner.core.metamodel.ZooKeeperClusterMetaModel

/**
 * @author yan@pongasoft.com  */
public class GluMetaModelImpl implements GluMetaModel
{
  Map<String, FabricMetaModel> fabrics

  @Override
  FabricMetaModel findFabric(String fabricName)
  {
    return fabrics[fabricName]
  }

  @Override
  AgentMetaModel findAgent(String fabricName, String agentName)
  {
    findFabric(fabricName)?.findAgent(agentName)
  }

  @Override
  Collection<AgentMetaModel> getAgents()
  {
    def agents = []

    fabrics.values().each { FabricMetaModel model ->
      agents.addAll(model.agents.values())
    }

    return agents
  }

  @Override
  ConsoleMetaModel findConsole(String consoleName)
  {
    fabrics.values().find { it.console.name == consoleName }?.console
  }

  @Override
  ZooKeeperClusterMetaModel findZooKeeperCluster(String zooKeeperClusterName)
  {
    fabrics.values().find { it.zooKeeperCluster.name == zooKeeperClusterName }?.zooKeeperCluster
  }

  @Override
  Map<String, ZooKeeperClusterMetaModel> getZooKeeperClusters()
  {
    Map<String, ZooKeeperClusterMetaModel> res = [:]

    fabrics.values().each { FabricMetaModel model ->
      res[model.zooKeeperCluster.name] = model.zooKeeperCluster
    }

    return res
  }

  @Override
  Map<String, ConsoleMetaModel> getConsoles()
  {
    Map<String, ConsoleMetaModel> res = [:]

    fabrics.values().each { FabricMetaModel model ->
      res[model.console.name] = model.console
    }

    return res
  }

  @Override
  Object toExternalRepresentation()
  {
    [
      agents: agents.collect { it.toExternalRepresentation() },
      consoles: consoles.values().collect { it.toExternalRepresentation() },
      zooKeeperClusters: zooKeeperClusters.values().collect { it.toExternalRepresentation() }
    ]
  }
}