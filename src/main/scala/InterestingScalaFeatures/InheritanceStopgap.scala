/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package InterestingScalaFeatures

object InheritanceStopgap extends App {

  trait GunModel {
    def loadBullets = true

    def point = true

    def shoot = true
  }

  //the behavior is inherited by Glock
  class Glock extends GunModel

  //the behavior is inherited but there is no loading bullets!
  //we need to disable this behavior!
  class LaserGun extends Glock

  trait GlockModel {
    this: GunModel =>
    def squeezeTrigger = shoot

    def aim = point
  }

  trait LaserGunModel {
    this: GlockModel =>
    def click = squeezeTrigger

    def loadLaser = true //cannot use loadBullets the inheritance stops here
  }

}
