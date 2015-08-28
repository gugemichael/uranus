find ./ -name "*.py" | xargs -i pychecker --only {}

find ./ -name "*.py" | xargs -i pylint {}
