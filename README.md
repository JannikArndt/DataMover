# DataMover

Out of the box scheduling, logging, monitoring and data governance.

[![Build Status](https://travis-ci.org/JannikArndt/DataMover.svg?branch=master)](https://travis-ci.org/JannikArndt/DataMover)

### Example:

```scala
import de.jannikarndt.datamover._
import scala.concurrent.duration._
import scala.language.postfixOps

object ExampleJob {
    def main(args: Array[String]): Unit = DataMover run classOf[Example] every (10 seconds)
}

class Example extends DataMover("Example") {

    override def run(): Unit = {
    
        // Logging
        logger.info("Logs are aggregated per run")

        // Write you own EXTRACT-function

        // Monitor your input
        monitor.input(5)
        
        // Write your own TRANSFORM-function

        // Write your own LOAD-function
        
        // Monitor your output
        monitor.output("Appended successfully")
    }
}
```

### To-Do / Planned

- Data Governance
- Alerting when job fails
- Uptime-Monitoring for sources and sinks

- Versioning: Which version of which job is deployed where? + Changelog
- Feature toggles

- Central server to monitor all jobs:
![Monitoring Idea](Resources/monitoring_idea.png)

- Interface for Prometheus/Grafana
- Interface for Elastic/Kibana
- Interface for Jolokia