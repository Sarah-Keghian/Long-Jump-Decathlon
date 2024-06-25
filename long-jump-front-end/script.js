const bouton_lancer = document.getElementById("bouton_lancer");
const bouton_saut = document.getElementById("bouton_saut");
const dice1 = document.getElementById("dice1");

bouton_lancer.addEventListener("click", function fetch_lancer() {
        fetch('https://mdn.github.io/learning-area/javascript/oojs/json/superheroes.json')
            .then(response => response.text())
            .then(response => {
                affiche_content(response)
            })
            .catch(error => {
                console.log("Il y a eu un problème avec l'opération fetch: " + error.message);
            })
});


function affiche_content(texte) {
    var conteneur = document.getElementById('content');
    var nouveauParagraphe = document.createElement("p");
    nouveauParagraphe.appendChild(document.createTextNode(texte))
    conteneur.appendChild(nouveauParagraphe);
}


bouton_saut.addEventListener("click", function() {
    
    bouton_saut.classList.add('invisible');
});

dice1.addEventListener("click", function() {
    
    dice1.classList.add('invisible');
});

