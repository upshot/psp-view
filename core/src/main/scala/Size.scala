package psp
package core

import Size._

class OverflowException extends RuntimeException

final class Size private (val value: Int) extends AnyVal with Ordered[Size] {
  private def checkSum(sum: Int): Size = try Size(sum) finally if (sum < value) fail(s"overflow: $value + ... == $sum")

  def compare(that: Size): Int = value compare that.value
  def + (n: Size): Size        = checkSum(value + n.value)
  def - (n: Size): Size        = Size(value - n.value)
  def * (n: Int): Size         = Size(value * n)
  def / (n: Int): Size         = if (n == 0) fail("division by zero") else Size(value / n)
  def min(that: Size): Size    = Size(value min that.value)
  def max(that: Size): Size    = Size(value max that.value)

  def isZero  = this == Zero
  def isError = this == NoSize

  def toInt: Int            = value
  def toLong: Long          = value
  def toOption: Option[Int] = if (isError) None else Some(toInt)

  override def toString = if (isError) "<no size>" else s"$value"
}

// Size is^Wshould be its own unapply (value class bugs drove us out for now)
object Size {
  final val Zero   = new Size(0)
  final val NoSize = new Size(-1)

  def apply(n: Int): Size = if (n <= 0) Zero else new Size(n)
  def unapply(s: Size)    = s.toOption

  private def fail(msg: String) = throw new ArithmeticException(msg)
}
