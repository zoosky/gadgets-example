import org.scalajs.dom
import org.querki.gadgets._
import rx._
import scalatags.JsDom.all._

class TransactionRangeGadget(implicit ctx:Ctx.Owner) extends Gadget[dom.html.Div] {
  val startDate = Var[Date](Date.now)
  val endDate = Var[Date](Date.now)
  val datePair = Rx { (startDate(), endDate()) }
  
  datePair.triggerLater {
    transactionsPane.foreach { tlist => tlist.updateWithDates(datePair.now) }
  }
  
  val transactionsPane = GadgetRef[TransactionList]
    .whenRendered { tlist =>
      tlist.updateWithDates(datePair.now)
    }

  def doRender() =
    div(
      p("Please specify the date range to show:"),
      span(
        new DatePickerGadget(startDate),
        " through ",
        new DatePickerGadget(endDate)
      ),
      
      transactionsPane <= new TransactionList()
    )
}