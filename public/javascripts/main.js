// public/javascripts/main.js
function updateGameState(game) {
    document.getElementById('current-word').textContent = game.currentWord;
    document.getElementById('used-words').textContent = Array.from(game.usedWords).join(', ');
    
    if (game.isGameOver) {
        document.getElementById('message').textContent = 'Game Over! The word ends with ん';
        document.getElementById('word-input').disabled = true;
    }
}

function submitWord() {
    const word = document.getElementById('word-input').value;
    
    fetch('/play', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Csrf-Token': window.csrfToken
        },
        body: JSON.stringify({ word: word })
    })
    .then(response => response.json())
    .then(data => {
        if (data.error) {
            document.getElementById('message').textContent = data.error;
        } else {
            updateGameState(data);
            document.getElementById('word-input').value = '';
            document.getElementById('message').textContent = '';
        }
    });
}

function resetGame() {
    fetch('/reset', { 
        method: 'POST',
        headers: {
            'Csrf-Token': window.csrfToken
        }
    })
    .then(response => response.json())
    .then(data => {
        updateGameState(data);
        document.getElementById('word-input').disabled = false;
        document.getElementById('message').textContent = '';
        document.getElementById('word-input').value = '';
    });
}

// Initialize game state
fetch('/state')
    .then(response => response.json())
    .then(data => updateGameState(data));
