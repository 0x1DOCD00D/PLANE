/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package DSLWorkshop

object CloudOrgDSL:
  case class CloudOrg(name: String, regions: Seq[Region])
  case class Region(name: String, zones: Seq[AvailabilityZone])
  case class AvailabilityZone(name: String, datacenters: Seq[DataCenter])
  case class DataCenter(name: String, hosts: Seq[Rack])
  case class Rack(name: String, servers: Seq[Server])
  case class Server(name: String, resources: Map[String, Int])
  def cloudOrg(name: String)(regions: Region*) = CloudOrg(name, regions)
  def region(name: String)(zones: AvailabilityZone*) = Region(name, zones)
  def availabilityZone(name: String)(datacenters: DataCenter*) = AvailabilityZone(name, datacenters)
  def dataCenter(name: String)(racks: Rack*) = DataCenter(name, racks)
  def rack(name: String)(servers: Server*) = Rack(name, servers)
  def server(name: String)(resources: (String, Int)*) = Server(name, resources.toMap)


  @main def simIt() =
    cloudOrg("myorg") {
      region("us-east") {
        availabilityZone("us-east-1") {
          dataCenter("dc1") {
            rack("rack1") {
              server("server1") {
                "cpu" -> 4
                "memory" -> 16
                "disk" -> 100
              }
              server("server2") {
                "cpu" -> 4
                "memory" -> 16
                "disk" -> 100
              }
            }
            rack("rack2") {
              server("server3") {
                "cpu" -> 4
                "memory" -> 16
                "disk" -> 100
              }
              server("server4") {
                "cpu" -> 4
                "memory" -> 16
                "disk" -> 100
              }
            }
          }
        }
        availabilityZone("us-east-2") {
          dataCenter("dc2") {
            rack("rack1") {
              server("server1") {
                "cpu" -> 4
                "memory" -> 16
                "disk" -> 100
              }
              server("server2") {
                "cpu" -> 4
                "memory" -> 16
                "disk" -> 100
              }
            }
            rack("rack2") {
              server("server3") {
                "cpu" -> 4
                "memory" -> 16
                "disk" -> 100
              }
              server("server4") {
                "cpu" -> 4
                "memory" -> 16
                "disk" -> 100
              }
            }
          }
        }
      }
      region("us-west") {
        availabilityZone("us-west-1") {
          dataCenter("dc1") {
            rack("rack1") {
              server("server1") {
                "cpu" -> 4
                "memory" -> 16
                "disk" -> 100
              }
              server("server2") {
                "cpu" -> 4
                "memory" -> 16
                "disk" -> 100
              }
            }
            rack("rack2") {
              server("server3") {
                "cpu" -> 4
                "memory" -> 16
                "disk" -> 100
              }
              server("server4") {
                "cpu" -> 4
                "memory" -> 16
                "disk" -> 100
              }
            }
          }
        }
        availabilityZone("us-west-2") {
          dataCenter("dc2") {
            rack("rack1") {
              server("server1") {
                "cpu" -> 4
                "memory" -> 16
                "disk" -> 100
              }
              server("server2") {
                "cpu" -> 4
                "memory" -> 16
                "disk" -> 100
              }
            }
            rack("rack2") {
              server("server3") {
                "cpu" -> 4
                "memory" -> 16
                "disk" -> 100
              }
              server("server4") {
                "cpu" -> 4
                "memory" -> 16
                "disk" -> 100
              }
            }
          }
        }
      }
    }