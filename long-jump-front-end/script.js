const bouton_lancer = document.getElementById("bouton_lancer");
const bouton_saut = document.getElementById("bouton_saut");
let dans_run_up = true
let de_elimine = [true, 1]
let actu_frozen = [false, false, false, false, false];

bouton_lancer.addEventListener("click", function fetch_lancer() {
    if (de_elimine[0]) {
        de_elimine = [false, 0]
        document.querySelectorAll(".dice").forEach((dice, index) => {
            actu_frozen[index] = false;
        });
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
                affiche_content(response)
            })
            .catch(error => {
                console.log("Il y a eu un problème avec l'opération fetch: " + error.message);
            })
    } else {
        alert("Il faut que vous éliminiez un dé !")
    }
});


// depend de l'info donnée : texte = [valeur_de_1, valeur_de_2, valeur_de_3, valeur_de_4, valeur_de_5]
function affiche_content(texte) {
    console.log(texte)
    document.getElementById('dice1').textContent = texte;
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
        if (conteneur_actif.contains(dice)) {
            conteneur_actif.removeChild(dice);
            conteneur_gele.appendChild(dice);
            actu_frozen[index] = true
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
            if (actu_frozen[index]) {
                conteneur_gele.removeChild(dice);
                conteneur_actif.appendChild(dice);
                actu_frozen[index] = false
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
    actu_frozen = [false, false, false, false, false]
    de_elimine = [true, 1]
    const conteneur_actif = document.getElementById("actif");
    const conteneur_gele = document.getElementById("gele");
    document.querySelectorAll(".dice").forEach((dice, index) => {
        if (conteneur_actif.contains(dice)) {
            conteneur_actif.removeChild(dice);
        }
        if (conteneur_gele.contains(dice)) {
            conteneur_gele.removeChild(dice);
            conteneur_actif.appendChild(dice);
        }
        actu_frozen[index] = false;
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
    });
}

