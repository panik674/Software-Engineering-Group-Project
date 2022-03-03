# Tom's guide to Git

## Naming Conventions
### Branches

```
name-sprint-what-it-does

ie, tjc-s1-add-data
```

### Commits

Basically you want to keep this short and easily readable.
- Try to use words like Add, Modify and Fix, rather than Adding, Fixing and Modifying
- Short and descriptive
- Capitalised

**Examples**
- Add log records (Impression, Click, Server) 
- Delete .idea 
- Fix typo
- Improve Data ingest function 

## Things to watch out for

**Do and Don't**
- Don't push to main, use a branch to avoid accidently pushing bad code or overwriting data.
- Always perform a pull command when you start working on a new task/branch, or before you push to the remote repo.
- Make sure when you checkout to main before creating a new branch.
- Do get someone to review your code before merging the branch to main.
- Do check the commit changes tab / git status to see what files you are committing before you commit.

**For the love of god don't**
- If you get odd error messages and unsure, ask don't try and force something through.
- Don't use **git push --force** unless you are unequivocally 100% certain about what that command will do.
