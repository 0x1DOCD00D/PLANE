
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package DesignPatterns

object AlertBridgeWithSelfType {

  // Implementation hierarchy

  trait AlertChannel {
    def send(recipient: String, message: String): Unit
  }

  trait EmailChannel extends AlertChannel {
    override def send(recipient: String, message: String): Unit =
      println(s"[EMAIL to $recipient] $message")
  }

  trait SlackChannel extends AlertChannel {
    override def send(recipient: String, message: String): Unit =
      println(s"[SLACK to $recipient] $message")
  }

  trait PagerDutyChannel extends AlertChannel {
    override def send(recipient: String, message: String): Unit =
      println(s"[PAGERDUTY to $recipient] $message")
  }

  // Abstraction hierarchy using a self type as the bridge
  abstract class Alert {
    this: AlertChannel =>

    def name: String
    def severity: String

    def fire(recipient: String, details: String): Unit = {
      val msg = s"[$severity] $name: $details"
      send(recipient, msg)
    }
  }

  // Refined abstractions bound to concrete channels

  class CpuAlertOverEmail extends Alert with EmailChannel {
    override val name = "CPU usage alert"
    override val severity = "HIGH"
  }

  class LatencyAlertOverSlack extends Alert with SlackChannel {
    override val name = "Latency alert"
    override val severity = "MEDIUM"
  }

  class BillingAlertOverPagerDuty extends Alert with PagerDutyChannel {
    override val name = "Billing anomaly alert"
    override val severity = "CRITICAL"
  }

  @main def runAlertBridgeWithSelfType(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/DesignPatterns/AlertBridgeWithSelfType.scala created at time 11:48AM")
    val cpu = new CpuAlertOverEmail
    val latency = new LatencyAlertOverSlack
    val billing = new BillingAlertOverPagerDuty

    cpu.fire("oncall@company.com", "CPU at 95 percent for 10 minutes")
    latency.fire("#oncall", "P99 latency above 500 ms")
    billing.fire("cfo@company.com", "Cost doubled compared to yesterday")
}
