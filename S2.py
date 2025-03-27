import re

# Token patterns
patterns = [
    (r'FORW', 'FORW'),
    (r'BACK', 'BACK'),
    (r'LEFT', 'LEFT'),
    (r'RIGHT', 'RIGHT'),
    (r'DOWN', 'DOWN'),
    (r'UP', 'UP'),
    (r'COLOR', 'COLOR'),
    (r'REP', 'REP'),
    (r'#[0-9A-Fa-f]{6}', 'HEX'),
    (r'[0-9]+', 'DECIMAL'),
    (r'"', 'QUOTE'),
    (r'\.', 'PERIOD'),
    (r'[^A-Za-z0-9#"\.\s]+', 'ERROR')
]

def tokenize(program):
    tokens = []
    position = 0

    
    while position < len(program):
        match = None
        for pattern, token_type in patterns:
            regex = re.compile(pattern)
            match = regex.match(program, position)
            if match:
                value = match.group(0)
                tokens.append((value, token_type))
                position = match.end(0)
                break
        
        if not match:
            position += 1

    return tokens