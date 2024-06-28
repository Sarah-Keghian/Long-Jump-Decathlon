document.addEventListener("DOMContentLoaded", function() {
    const playerName = localStorage.getItem('playerName');
    console.log("Player Name:", playerName);
    fetch('/api/Partie/getLeaders', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            const leaderboardDiv = document.getElementById('leaderboard');
            data.forEach((player, index) => {
                const playerDiv = document.createElement('div');
                playerDiv.classList.add('player');
                playerDiv.textContent = `${index + 1}. ${player.name} - ${player.score} points`;
                leaderboardDiv.appendChild(playerDiv);
            });
        })
        .catch(error => {
            console.error('Error fetching leaderboard data:', error);
        });
});