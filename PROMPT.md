# ISSUE-DRIVEN AFK WORKFLOW

Use [$tdd](/Users/vladimirpopov/.agents/skills/tdd/SKILL.md) for implementation discipline.

You are working in the current repository. Issues are tracked in GitHub Issues for `vova-white/chereshniy-bot`.

## STARTUP CONTEXT

First, collect the current work context yourself.

Run:

```bash
git log -n 5 --format="%H%n%ad%n%B---" --date=short 2>/dev/null || echo "No commits found"
```

Then fetch open GitHub issues:

```bash
gh issue list \
  --repo vova-white/chereshniy-bot \
  --state open \
  --limit 100 \
  --json number,title,body,labels,assignees,updatedAt,url
```

Only consider issues labeled `ready-for-agent`.

Do not work on issues labeled `ready-for-human`.

If `gh` is unavailable, unauthenticated, or the issue list cannot be fetched, stop and report the blocker.

Review the last commits and the open `ready-for-agent` issues before selecting work.

## TASK SELECTION

Pick exactly one issue.

Prioritize tasks in this order:

1. Critical bugfixes
2. Development infrastructure
3. Tracer bullets for new features
4. Polish and quick wins
5. Refactors

Development infrastructure such as tests, types, dev scripts, architecture shell, and CI readiness is an important precursor to feature work.

Tracer bullets are small end-to-end slices of functionality that validate the architecture before expanding the feature.

Before starting, fetch the full selected issue:

```bash
gh issue view <number> \
  --repo vova-white/chereshniy-bot \
  --json number,title,body,labels,comments,url
```

If the issue is blocked by another open issue, skip it and choose the next eligible `ready-for-agent` issue.

If there are no eligible unblocked `ready-for-agent` issues, output exactly:

```xml
<promise>NO MORE TASKS</promise>
```

## EXPLORATION

Explore the repo before editing.

Read relevant docs first:

- `AGENTS.md`
- `CONTEXT-MAP.md` if present
- relevant `CONTEXT.md` files
- relevant ADRs in `docs/adr/`
- architecture in `docs/architecture.md` 
- the selected GitHub issue body and comments
- the last five commits collected above

Use the repository's domain language in tests, code, and commit messages.

## IMPLEMENTATION

Work on only the selected issue.

Use TDD where practical:

1. Write one behavior-focused test through a public/application interface.
2. Run it and confirm it fails for the expected reason.
3. Implement the minimum code to pass.
4. Repeat one behavior at a time.
5. Refactor only after tests are green.

Prefer vertical slices and tracer bullets over broad horizontal implementation.

If the issue is ambiguous, make a conservative assumption based on docs and ADRs. If the ambiguity could cause wrong product behavior or data loss, stop and comment on the issue instead of guessing.

## FEEDBACK LOOPS

Before committing, run the relevant feedback loops.

At minimum, run the checks relevant to the files you changed.

For frontend changes:

```bash
pnpm --dir frontend test:unit
pnpm --dir frontend type-check
```

For backend changes:

```bash
./backend/gradlew -p backend test
```

For changes that touch both frontend and backend, run both frontend checks and the backend test command.

If a feedback loop cannot run, report exactly why.

## COMMIT

Make one git commit for the selected issue.

The commit message must include:

1. Issue number and title
2. Key decisions made
3. Files changed
4. Tests/feedback loops run
5. Blockers or notes for next iteration, if any

## GITHUB ISSUE UPDATE

If the task is complete:

1. Add a GitHub issue comment summarizing:
   - what changed
   - tests run
   - commit hash
2. Close the issue.

Use:

```bash
gh issue comment <number> --repo vova-white/chereshniy-bot --body "<summary>"
gh issue close <number> --repo vova-white/chereshniy-bot
```

If the task is not complete:

1. Add a GitHub issue comment with:
   - what was done
   - what remains
   - blocker, if any
   - tests run
2. Leave the issue open.

## FINAL RESPONSE

Report:

- selected issue number and title
- commit hash, if committed
- tests run
- whether the issue was closed or left open
- any blocker

## FINAL RULES

ONLY WORK ON A SINGLE TASK.

Do not work on `ready-for-human` issues.

Do not modify unrelated files.

Do not revert user changes.

Do not create broad refactors unless the selected issue requires them.
