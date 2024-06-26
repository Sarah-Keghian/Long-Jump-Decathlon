document.addEventListener("DOMContentLoaded", function() {
    const bouton_lancer = document.getElementById("bouton_lancer");
    const bouton_saut = document.getElementById("bouton_saut");
    const bouton_suivant = document.getElementById("bouton_suivant");
    const diceIds = ["dice1", "dice2", "dice3", "dice4", "dice5"];
    const diceElements = diceIds.map(id => document.getElementById(id));

    let playerName = prompt("Entrez votre nom :");
    let id_joueur = 0
    let id_partie = 0
    let place = 0
    if (!playerName) {
        playerName = "Joueur";
    }

    localStorage.setItem('playerName', playerName);

    fetch('/api/Joueur/create', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: playerName
    })
        .then(response => response.json())
        .then(response => {
            id_joueur = response
            fetch('/api/Partie/create', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: id_joueur
            })
                .then(response => response.json())
                .then(response => {
                    id_partie = response["id"]
                    fetch('/api/Essais/create', {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: id_partie
                    })
                        .then(response => response.json())
                        .then(response => {
                            id_essai = response["id"]
                        })
                        .catch(error => {
                            console.log("Il y a eu un problème avec l'opération fetch: " + error.message);
                        })
                })
                .catch(error => {
                    console.log("Il y a eu un problème avec l'opération fetch: " + error.message);
                })
        })
        .catch(error => {
            console.log("Il y a eu un problème avec l'opération fetch: " + error.message);
        })

    let dans_run_up = true
    let de_elimine = [true, 1]
    let actu_frozen = [false, false, false, false, false];
    let id_des = [0, 0, 0, 0, 0]
    let id_groupe = 0
    let des_jetables = false
    start()

    function start() {
        fetch('/api/GroupeDes/create', {
            method: 'GET'
        })
            .then(response => response.json())
            .then(response => {
                id_des[0] = response["listeDes"]["0"]["id"]
                id_des[1] = response["listeDes"]["1"]["id"]
                id_des[2] = response["listeDes"]["2"]["id"]
                id_des[3] = response["listeDes"]["3"]["id"]
                id_des[4] = response["listeDes"]["4"]["id"]
                id_groupe = response["id"]
            })
            .catch(error => {
                console.log("Il y a eu un problème avec l'opération fetch: " + error.message);
            })
    }

    bouton_lancer.addEventListener("click", function fetch_lancer() {
        let lancable = false
        document.querySelectorAll(".dice, .dice-frozen").forEach((dice, index) => {
            if (conteneur_actif.contains(dice)) {
                lancable = true
            }

        })
        if (de_elimine[0]) {
            de_elimine = [false, 0]
            des_jetables = true
            document.querySelectorAll(".dice, .dice-frozen").forEach((dice, index) => {
                if (conteneur_gele.contains(dice)) {
                    dice.classList.remove("dice")
                    dice.classList.add("dice-frozen")
                }
                actu_frozen[index] = false;
            });

            if (lancable) {
                fetch('/api/GroupeDes/throw', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: id_groupe
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error(`HTTP error! Status: ${response.status}`);
                        }
                        return response.json();
                    })
                    .then(response => {
                        affiche_content(response)
                    })
                    .catch(error => {
                        console.log("Il y a eu un problème avec l'opération fetch: " + error.message);
                    })
            } else {
                alert("Il n'y a pas de dé à jeter !")
                de_elimine = [true, 1]
            }
        } else {
            alert("Il faut que vous éliminiez un dé !")
        }
    });


    function affiche_content(texte) {
        document.getElementById('dice1').textContent = texte["listeDes"]["0"]["position"];
        document.getElementById('dice2').textContent = texte["listeDes"]["1"]["position"];
        document.getElementById('dice3').textContent = texte["listeDes"]["2"]["position"];
        document.getElementById('dice4').textContent = texte["listeDes"]["3"]["position"];
        document.getElementById('dice5').textContent = texte["listeDes"]["4"]["position"];
}


    const conteneur_actif = document.getElementById("actif");
    const conteneur_gele = document.getElementById("gele");
    bouton_saut.addEventListener("click", function () {
        saut_possible = false
        saut_oblige = true
        document.querySelectorAll(".dice").forEach((dice, index) => {
            if (conteneur_gele.contains(dice)) {
                saut_possible = true
            }
            if (conteneur_actif) {
                saut_oblige = false
            }
        })
        if ((de_elimine[0] && saut_possible) || saut_oblige) {
            if (document.getElementById('score_run_up').textContent > 8) {
                alert("Votre score de Run up est superieur à 8 !")
                reset_game()
            } else {
                prep_round_jump()
            }
        } else {
            alert("Il faut que vous éliminiez un dé !")
        }
    });

    bouton_suivant.addEventListener("click", function () {
        let possible = false
        document.querySelectorAll(".dice").forEach((dice, index) => {
            if (conteneur_gele.contains(dice)) {
                possible = true
            }
        })
        if (possible) {
            reset_game()
        }
    })

    document.querySelectorAll(".dice").forEach((dice, index) => {
        dice.addEventListener("click", function () {
            if (conteneur_actif.contains(dice) && des_jetables) {
                conteneur_actif.removeChild(dice);
                conteneur_gele.appendChild(dice);
                actu_frozen[index] = true
                freeze(id_des[index])
                de_elimine[0] = true
                de_elimine[1] = de_elimine[1] + 1
                if (dans_run_up) {
                    if (document.getElementById('score_run_up').textContent == 0) {
                        document.getElementById('score_run_up').textContent = parseInt(dice.textContent)
                    } else {
                        document.getElementById('score_run_up').textContent = parseInt(document.getElementById('score_run_up').textContent) + parseInt(dice.textContent)
                    }
                } else {
                    if (document.getElementById('score_jump').textContent == 0) {
                        document.getElementById('score_jump').textContent = parseInt(dice.textContent)
                    } else {
                        document.getElementById('score_jump').textContent = parseInt(document.getElementById('score_jump').textContent) + parseInt(dice.textContent)
                    }
                }
            } else {
                if (actu_frozen[index] && des_jetables) {
                    conteneur_gele.removeChild(dice);
                    conteneur_actif.appendChild(dice);
                    actu_frozen[index] = false
                    unfreeze(id_des[index])
                    if (de_elimine[0]) {
                        de_elimine[1] = de_elimine[1] - 1
                        if (de_elimine[1] <= 0) {
                            de_elimine = [false, 0]
                        }
                        if (dans_run_up) {
                            document.getElementById('score_run_up').textContent = parseInt(document.getElementById('score_run_up').textContent) - parseInt(dice.textContent)
                        } else {
                            document.getElementById('score_jump').textContent = parseInt(document.getElementById('score_jump').textContent) - parseInt(dice.textContent)
                        }
                    }
                }
            }
        });
    });

    function prep_round_jump() {
        bouton_saut.classList.add('invisible');
        bouton_suivant.classList.remove('invisible');
        dans_run_up = false
        de_elimine = [true, 1]
        const conteneur_actif = document.getElementById("actif");
        const conteneur_gele = document.getElementById("gele");
        des_jetables = false
        diceElements.forEach((dice, index) => {
            if (conteneur_actif.contains(dice)) {
                dice.style.display = 'none';
                freeze(id_des[index]);
            }
            if (conteneur_gele.contains(dice)) {
                conteneur_gele.removeChild(dice);
                conteneur_actif.appendChild(dice);
                dice.classList.remove("dice-frozen");
                dice.classList.add("dice");
                actu_frozen[index] = false;
                dice.textContent = "?";
                unfreeze(id_des[index]);
            }
        });
    }

    function reset_game() {
        if (parseInt(document.getElementById('score_total').textContent) < parseInt(document.getElementById('score_jump').textContent)) {
            document.getElementById('score_total').textContent = parseInt(document.getElementById('score_jump').textContent)
        }
        document.getElementById('essai').textContent = parseInt(document.getElementById('essai').textContent) - 1
        const score_jump = parseInt(document.getElementById('score_jump').textContent)
        if (document.getElementById('essai').textContent >= 1) {
            bouton_saut.classList.remove('invisible');
            bouton_suivant.classList.add('invisible');
            dans_run_up = true
            document.getElementById('score_run_up').textContent = 0;
            document.getElementById('score_jump').textContent = 0;
            de_elimine = [true, 1]
            fetch('/api/Essais/addEssai', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({"id": id_essai, "score": score_jump})
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }
                    return response.json();
                })
                .catch(error => {
                    console.log("Il y a eu un problème avec l'opération fetch: " + error.message);
                })
            const conteneur_actif = document.getElementById("actif");
            const conteneur_gele = document.getElementById("gele");
            des_jetables = false
            diceElements.forEach((dice, index) => {
                dice.style.display = 'flex';
                dice.textContent = "?";
                unfreeze(id_des[index]);
                if (conteneur_gele.contains(dice)) {
                    conteneur_gele.removeChild(dice);
                    conteneur_actif.appendChild(dice);
                    dice.classList.remove("dice-frozen");
                    dice.classList.add("dice");
                    actu_frozen[index] = false;
                }
            });
        } else {
            bouton_suivant.classList.add('invisible');
            bouton_saut.classList.add('invisible');
            bouton_lancer.classList.add('invisible');
            bouton_leaderboard.classList.remove('invisible')
            let score = parseInt(document.getElementById('score_total').textContent)
            fetch('/api/Partie/addScoreFinal', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({"id": id_partie, "score": score})
            })
                .then(response => response.json())
                .then(response => {
                    place = response["place"]
                    localStorage.setItem('place', place);
                })
                .catch(error => {
                    console.log("Il y a eu un problème avec l'opération fetch: " + error.message);
                })
            fetch('/api/GroupeDes/delete', {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(id_groupe)
            })
                .catch(error => {
                    console.error("There was a problem with the delete operation:", error.message);
                });
            affiche_leaderboard()
        }
    }

    bouton_leaderboard = document.getElementById("bouton_leaderboard")
    bouton_leaderboard.addEventListener("click", function() {
        window.open('leaderboard.html', '_blank');
    });

    function affiche_leaderboard() {

    }

    function freeze(id_de) {
        fetch('/api/GroupeDes/freeze', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({"id": id_groupe, "idDe": id_de})
        })
            .then(response => response.json())
            .then(response => {
            })
            .catch(error => {
                console.log("Il y a eu un problème avec l'opération fetch: " + error.message);
            })
    }

    function unfreeze(id_de) {
        fetch('/api/GroupeDes/unFreeze', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({"id": id_groupe, "idDe": id_de})
        })
            .then(response => response.json())
            .then(response => {
            })
            .catch(error => {
                console.log("Il y a eu un problème avec l'opération fetch: " + error.message);
            })
    }
});
