# Arduino Thermo Hygrometer GitHub Configuration

Repository ownership, dependency updates, continuous integration, and code scanning.

## Overview

This directory contains GitHub-specific configuration for the API and frontend.
Workflow behavior and current CI limitations are documented in the
[workflow guide](workflows/README.md).

## Requirements

- GitHub Actions enabled for workflow execution.
- Code scanning available with the permissions requested by the CodeQL workflow.
- Dependabot version updates enabled for scheduled dependency pull requests.

## Getting started

Changes to `api/` or `app/` trigger their respective CI workflows on pull requests and
pushes to `main`. CodeQL also runs on a schedule. These workflows do not currently expose
a manual `workflow_dispatch` trigger.

Run the relevant [API](../api/README.md#development) or
[frontend](../app/README.md#development) checks locally before opening a pull request.

## Configuration

### Ownership

[`CODEOWNERS`](CODEOWNERS) assigns the repository owner to all files, including the API and
frontend. Required code-owner approval depends on repository branch-protection settings;
it is not established by `CODEOWNERS` alone.

### Dependency updates

[`dependabot.yml`](dependabot.yml) configures GitHub Actions, npm, and Maven updates.
Its current cron expression, `0 8 1-7 * *`, runs at 08:00 UTC on each of the first seven
days of every month, not once per month.

| Ecosystem      | Configured directory | Current coverage                                           |
| -------------- | -------------------- | ---------------------------------------------------------- |
| GitHub Actions | `/`                  | Workflow action dependencies                               |
| Maven          | `/api`               | API dependencies and plugins                               |
| npm            | `/`                  | No root package manifest; frontend manifests are elsewhere |

The npm entry does not target `app/package.json` or the separately locked
`app/tools/openapi/package.json`. Covering both requires corresponding Dependabot directory
entries. This is a configuration follow-up; the README does not change that behavior.

## Project structure

```text
.github/
  CODEOWNERS            Repository ownership
  dependabot.yml        Scheduled dependency updates
  workflows/
    backend-ci.yml      API quality and integration-test jobs
    frontend-ci.yml     Frontend quality and test job
    codeql.yml          Java and JavaScript/TypeScript code scanning
    README.md           Workflow details and limitations
```

## Development

When changing dependencies or build scripts, check the workflow commands, caches, path
filters, and runtime versions against the relevant manifest. Document configured behavior
separately from planned improvements.

Application code and tool configurations live in their own directories. See the
[workflow guide](workflows/README.md#configuration) for checks not yet covered by automation.

## Related documentation

- [Repository overview](../README.md)
- [API guide](../api/README.md)
- [Frontend guide](../app/README.md)
- [Workflow guide](workflows/README.md)

## License

MIT. See [LICENSE](../LICENSE).
