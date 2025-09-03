package scala3Features.ProductSerializable

object PS_v2 {
  import java.io.*
  import java.nio.file.{Files, Path, StandardOpenOption}

  object JavaSer:
    def toBytes[A <: Serializable](a: A): Array[Byte] =
      val baos = new ByteArrayOutputStream()
      val oos = new ObjectOutputStream(baos)
      oos.writeObject(a)
      oos.close()
      baos.toByteArray

    def fromBytes[A](bs: Array[Byte]): A =
      val ois = new ObjectInputStream(new ByteArrayInputStream(bs))
      val obj = ois.readObject().asInstanceOf[A]
      ois.close()
      obj

  final class DurableQueue[A <: Product & Serializable](path: Path):
    def append(a: A): Unit =
      val bytes = JavaSer.toBytes(a)
      val out = new DataOutputStream(
        Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)
      )
      try
        out.writeInt(bytes.length)
        out.write(bytes)
      finally out.close()

    def foreach(consume: A => Unit): Unit =
      if !Files.exists(path) then return
      val in = new DataInputStream(Files.newInputStream(path))
      try
        while true do
          val len = in.readInt() // throws EOFException at end
          val buf = new Array[Byte](len)
          in.readFully(buf)
          consume(JavaSer.fromBytes[A](buf))
      catch case _: EOFException => ()
      finally in.close()

  // --- Example ADT root: serializable at the supertype boundary
  sealed trait Command extends Product with Serializable

  final case class Create(id: String, amount: BigDecimal) extends Command

  final case class Cancel(id: String) extends Command

  def main(args: Array[String]): Unit =
    import JavaSer.*
    val log: Path = Path.of("commands.log")
    val q: DurableQueue[Command] = new DurableQueue[Command](log)
    q.append(Create("ord-1", BigDecimal(12.34)))
    q.append(Cancel("ord-1"))

    println("Replaying:")
    q.foreach(c => println(s" -> $c"))
    println("Replaying:")
    q.foreach(c => println(s" -> $c"))
    Files.deleteIfExists(log)

}
