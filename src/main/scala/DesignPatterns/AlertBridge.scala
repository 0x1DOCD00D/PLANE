
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package DesignPatterns

object AlertBridge {

/*
Here is a concrete Bridge example that looks like something you might actually use
in a cloud system instead of a toy Shape Color demo.

You have alerts like High CPU, High Latency, Billing Anomaly.
You want to send them over different channels Email, Slack, PagerDuty, maybe more later.
You want to be able to add new alert types and new channels independently, without combinatorial subclass hell.

That is exactly what the Bridge pattern is for.
* */

  trait AlertChannel {
    def send(recipient: String, message: String): Unit
  }

  class EmailChannel extends AlertChannel {
    override def send(recipient: String, message: String): Unit =
      println(s"[EMAIL to $recipient] $message")
  }

  class SlackChannel extends AlertChannel {
    override def send(recipient: String, message: String): Unit =
      println(s"[SLACK to $recipient] $message")
  }

  class PagerDutyChannel extends AlertChannel {
    override def send(recipient: String, message: String): Unit =
      println(s"[PAGERDUTY to $recipient] $message")
  }

  // Abstraction hierarchy: "what" the alert represents

  abstract class Alert(channel: AlertChannel) {
    def name: String
    def severity: String

    def fire(recipient: String, details: String): Unit = {
      val msg =
        s"[$severity] $name: $details"
      channel.send(recipient, msg)
    }
  }

  class CpuAlert(channel: AlertChannel) extends Alert(channel) {
    override val name = "CPU usage alert"
    override val severity = "HIGH"
  }

  class LatencyAlert(channel: AlertChannel) extends Alert(channel) {
    override val name = "Latency alert"
    override val severity = "MEDIUM"
  }

  class BillingAnomalyAlert(channel: AlertChannel) extends Alert(channel) {
    override val name = "Billing anomaly alert"
    override val severity = "CRITICAL"
  }

  @main def runAlertBridge(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/DesignPatterns/AlertBridge.scala created at time 11:42AM")
    val email = new EmailChannel
    val slack = new SlackChannel

    val cpuOverEmail = new CpuAlert(email)
    val latencyOverSlack = new LatencyAlert(slack)
    val billingOverEmail = new BillingAnomalyAlert(email)

    cpuOverEmail.fire("oncall@company.com", "CPU at 95 percent for 10 minutes")
    latencyOverSlack.fire("#oncall", "P99 latency above 500 ms")
    billingOverEmail.fire("cfo@company.com", "Cost doubled compared to yesterday")
}
