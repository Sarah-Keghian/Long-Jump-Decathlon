const bouton_lancer = document.getElementById("bouton_lancer");
const bouton_saut = document.getElementById("bouton_saut");
let dans_run_up = true
let de_elimine = [true, 1]
let actu_frozen = [false, false, false, false, false];
let id_des = [0, 0, 0, 0, 0]
let id_groupe = 0
let des_jetables = false
start()

function start() {
    const dataToSend = 5
    fetch('/api/GroupeDes/create', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: dataToSend
    })
        .then(response => response.json())
        .then(response => {
            id_des[0] = response["listeDes"]["0"]["id"]
            id_des[1] = response["listeDes"]["1"]["id"]
            id_des[2] = response["listeDes"]["2"]["id"]
            id_des[3] = response["listeDes"]["3"]["id"]
            id_des[4] = response["listeDes"]["4"]["id"]
            id_groupe = response["id"]
            console.log(id_groupe, id_des, response)
        })
        .catch(error => {
            console.log("Il y a eu un problème avec l'opération fetch: " + error.message);
        })
}

bouton_lancer.addEventListener("click", function fetch_lancer() {
    if (de_elimine[0]) {
        de_elimine = [false, 0]
        des_jetables = true
        document.querySelectorAll(".dice").forEach((dice, index) => {
            actu_frozen[index] = false;
        });
        fetch('/api/GroupeDes/throw', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: id_groupe
        })
            .then(response => response.json())
            .then(response => {
                console.log(response)
                affiche_content(response)
            })
            .catch(error => {
                console.log("Il y a eu un problème avec l'opération fetch: " + error.message);
            })
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
    console.log(texte)
}

bouton_saut.addEventListener("click", function() {
    if (de_elimine[0]) {
        if (document.getElementById('score_run_up').textContent > 8){
            alert("Votre score de Run up est superieur à 8 !")
            reset_game()
        } else {
            prep_round_jump()
        }
    } else {
        alert("Il faut que vous éliminiez un dé !")
    }
});

const conteneur_actif = document.getElementById("actif");
const conteneur_gele = document.getElementById("gele");
document.querySelectorAll(".dice").forEach((dice, index) => {
    dice.addEventListener("click", function() {
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
    dans_run_up = false
    de_elimine = [true, 1]
    const conteneur_actif = document.getElementById("actif");
    const conteneur_gele = document.getElementById("gele");
    des_jetables = false
    document.querySelectorAll(".dice").forEach((dice, index) => {
        if (conteneur_actif.contains(dice)) {
            conteneur_actif.removeChild(dice);
            freeze(id_des[index])
        }
        if (conteneur_gele.contains(dice)) {
            conteneur_gele.removeChild(dice);
            conteneur_actif.appendChild(dice);
            unfreeze(id_des[index])
        }
        actu_frozen[index] = false;
        dice.textContent = "?";
    });
}

function reset_game() {
    dans_run_up = true;
    const conteneur_actif = document.getElementById("actif");
    const conteneur_gele = document.getElementById("gele");
    document.getElementById('score_run_up').textContent = 0;
    document.getElementById('score_jump').textContent = 0;
    document.getElementById('score_total').textContent = 0;
    document.querySelectorAll(".dice").forEach((dice, index) => {
        if (conteneur_gele.contains(dice)) {
            conteneur_gele.removeChild(dice);
            conteneur_actif.appendChild(dice);
        }
        actu_frozen[index] = false;
        unfreeze(id_des[index])
    });
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
            console.log("response freeze :", response)
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
            console.log("response unfreeze :", response)
        })
        .catch(error => {
            console.log("Il y a eu un problème avec l'opération fetch: " + error.message);
        })
}
