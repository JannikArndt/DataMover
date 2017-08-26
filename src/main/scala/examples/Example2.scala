package examples

import de.jannikarndt.datamover.Job

object Example2 extends Job {
    override val jobName = "Example2"

    override def run(): Unit = {
        logger.info("In Job")
    }


}
