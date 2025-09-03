package scala3Features.ProductSerializable

import scala3Features.ProductSerializable.PS_v2.JavaSer

object PS_v5 {
  def toCliFlags(p: Product): List[String] =
    val n = p.productArity
    val xs = (0 until n).flatMap { i =>
      val k = p.productElementName(i)
      val v = p.productElement(i)
      List(s"--$k", v.toString)
    }
    xs.toList

  sealed trait JobCfg extends Product with Serializable

  final case class TrainCfg(epochs: Int, lr: Double, save: Boolean) extends JobCfg

  final case class ServeCfg(host: String, port: Int, ssl: Boolean) extends JobCfg

  def launch(cfg: JobCfg): Unit =
    val cmd = "job-bin" :: toCliFlags(cfg)
    println(cmd.mkString(" "))
    // round-trip to prove replayability by the supertype
    val bytes = JavaSer.toBytes(cfg)
    val same = JavaSer.fromBytes[JobCfg](bytes)
    assert(cfg == same)

  @main def cliDemo(): Unit =
    launch(TrainCfg(epochs = 20, lr = 0.001, save = true))
    launch(ServeCfg(host = "0.0.0.0", port = 9000, ssl = false))
}
