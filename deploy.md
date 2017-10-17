# Deployment

## Continuous Integration

CI is run by [Travic-CI](https://travis-ci.org/JannikArndt/DataMover).

## Snapshot
Run 

```bash
mvn clean deploy
```

Snapshots are deployed at [oss.sonatype.org](https://oss.sonatype.org/content/repositories/snapshots/de/jannikarndt/datamover_2.12/).


## Release

Remove the `-SNAPSHOT` from the version in `pom.xml` and run

```bash
mvn clean package source:jar gpg:sign install:install deploy:deploy
```

and then head to [sonatype.org](https://oss.sonatype.org/#stagingRepositories), `Close` the repository and `Release` it.

Releases are deployed at [maven.org](https://repo1.maven.org/maven2/de/jannikarndt/datamover_2.12/).
