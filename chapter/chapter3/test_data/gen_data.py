import json
import random
import uuid

name = ('Tom', 'Jerry', 'Jim', 'Angela', 'Ann', 'Bella', 'Bonnie', 'Caroline')
hobby = ('reading', 'play', 'dancing', 'sing')
subject = ('math', 'chinese', 'english', 'computer')

data = []
for item in name:
    scores = {key: random.randint(60, 100) for key in subject}
    data.append("|".join([uuid.uuid4().hex, item, ','.join(
        random.sample(set(hobby), 2)), ','.join(["{0}:{1}".format(k, v) for k, v in scores.items()])]))

with open('test.csv', 'w') as f:
    f.write('\n'.join(data))

