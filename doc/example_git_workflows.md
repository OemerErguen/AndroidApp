# Example git workflows


## Git Workflow 1: update local master and checkout new branch "feature/id_name" from it
```
# only - if not already on master
git checkout master

git fetch --all
git pull origin master

git checkout -b feature/id_name
# do the work
git push -u origin feature/id_name
```

## Git workflow 2: daily sync with master of your active local branches + local master
```
git checkout <your_branch_to_update>
git fetch --all
git pull origin master
```

## Git workflow 3: tag a release
```
# make sure to have the version to be tagged checked out and no other local (not pushed) tags around
git tag v0.X
git push --tags
```