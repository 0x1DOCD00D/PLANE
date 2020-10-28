/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

@Ignore
public class ConfigTest {
    @Test
    public void test() {
<<<<<<< HEAD
        new ConfigClient("ip-10-17-129-163.dev.org.com").configureForEnvironment();
=======
        new ConfigClient("ip-10-17-133-90.dev.org.com").configureForEnvironment();
>>>>>>> 05c561a606c15b7a5cc1a6e12305af7a642f4fcc
    }

    @Test
    public void testPom() {
        PomConfigurator pomConfigurator = new PomConfigurator();
        pomConfigurator.setFalcon_atdd_version("7.10.20.mini-SNAPSHOT");
        pomConfigurator.setMsgw_atdd_version("1.7.1.15");
        pomConfigurator.setMdg_atdd_version("7.2.0.1");

        pomConfigurator.updateFrameworkVersions(false);
    }
}
