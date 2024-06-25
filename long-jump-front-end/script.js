const bouton_lancer = document.getElementById("bouton_lancer");
const bouton_saut = document.getElementById("bouton_saut");
let dans_run_up = true

bouton_lancer.addEventListener("click", function fetch_lancer() {
        fetch('https://mdn.github.io/learning-area/javascript/oojs/json/superheroes.json')
            .then(response => response.json())
            .then(response => {
                affiche_content(response["members"][0]["age"])
            })
            .catch(error => {
                console.log("Il y a eu un problème avec l'opération fetch: " + error.message);
            })
});


// depend de l'info donnée : texte = [valeur_de_1, valeur_de_2, valeur_de_3, valeur_de_4, valeur_de_5]
function affiche_content(texte) {
    document.getElementById('dice1').textContent = texte;
}

bouton_saut.addEventListener("click", function() {
    bouton_saut.classList.add('invisible');
    dans_run_up = false
});

document.querySelectorAll(".dice").forEach(dice => {
    dice.addEventListener("click", function() {
        const conteneur_actif = document.getElementById("actif");
        const conteneur_gele = document.getElementById("gele");
        if (conteneur_actif.contains(dice)) {
            conteneur_actif.removeChild(dice);
            conteneur_gele.appendChild(dice);
        };
        if (dans_run_up) {
            if (document.getElementById('score_run_up').textContent == 0) {
                document.getElementById('score_run_up').textContent = parseInt(dice.textContent)
            }
            else {
                document.getElementById('score_run_up').textContent = parseInt(document.getElementById('score_run_up').textContent) + parseInt(dice.textContent)
            }
        };
    });
});
