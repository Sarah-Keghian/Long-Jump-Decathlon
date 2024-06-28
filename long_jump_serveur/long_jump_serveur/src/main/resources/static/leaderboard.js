document.addEventListener("DOMContentLoaded", function() {
    const playerName = localStorage.getItem('playerName');
    const place = localStorage.getItem('place');
    console.log("Player Name:", playerName, place);
    fetch('/api/Partie/getLeaders', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            console.log("data", data)
            const leaderboardDiv = document.getElementById('leaderboard');
            data.forEach((player, index) => {
                console.log(player, index)
                const playerDiv = document.createElement('div');
                playerDiv.classList.add('player');
                playerDiv.textContent = player.place + ". " + player.nomJoueur + " - " + player.score + " points";
                leaderboardDiv.appendChild(playerDiv);
            });
        })
        .catch(error => {
            console.error('Error fetching leaderboard data:', error);
        });
});