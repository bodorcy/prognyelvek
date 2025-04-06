import scala.annotation.tailrec

object gyak2 extends App{

  def fibNemJo(N: Int): Int = {
    N match {
      case 0 => 0
      case 1 | 2 => 1
      case _ => fibNemJo(N-1) + fibNemJo(N-2)
    }
  }

  def fib(N: Int): Int = {
    @tailrec
    def fib(N: Int, last: Int, lastlast:  Int): Int = {
      N match{
        case 0 => last
        case _ => println(last+lastlast); fib(N-1, last + lastlast, last)
      }
    }
    fib(N,1, 0)
  }

  def szjegyosszeg(num:Int): Int = {
    @tailrec
    def szjegyosszeg(num:String, sum:Int): Int = {
      if (num.isEmpty)
        sum
      else
        szjegyosszeg(num.substring(0, num.length - 1), sum + num.last - 48)
    }
    szjegyosszeg(num.toString(), 0)
  }

  def validTriangle(a:Float, b:Float, c:Float): Boolean = {
    @tailrec
    def validTriangle(a:Float, b:Float, c:Float, isTriangle:Boolean, combinations:Int): Boolean = {
      if(combinations == 0)
        isTriangle
      else
        validTriangle(c, a, b, isTriangle && (a + b > c), combinations - 1)
    }
      validTriangle(a, b, c, true, 3)
  }

  def sqr(N: Int): Int =  {
    @tailrec
    def sqr(N: Int, sum: Int): Int = {
      if (N == 1) sum + 1
      else sqr(N-1, sum + N*N)
     }
    sqr(N, 0)
  }

  def mul(a: Int, b: Int): Int = {
    @tailrec
    def mul(a: Int, b: Int, sum: Int): Int = {
      if (b == 0) sum
      else mul(a, b-1, sum + a)
    }
    mul(a, b, 0)
  }

  def heron(a:Double, b:Double, c:Double):Double = {
    @tailrec
    def heron(a:Double, b:Double, c:Double, prod:Double, cnt:Int): Double = {
      if (cnt == 0)
        prod
      else{
        val s = (a + b + c) / 2
        heron(c, a, b, prod * math.sqrt(math.pow(s,2) - s * a), cnt - 1)
      }
    }
    heron(a, b, c, 1, 3)
  }
  println(heron(3,4,5))
}
