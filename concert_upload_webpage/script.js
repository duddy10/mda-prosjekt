let concerts = [];

function onHandleLogin(element){
    let u = document.getElementById("loginUsername").value;
    let p = document.getElementById("loginPassword").value;

    console.log("Username: " + u + "\nPassword: " + p);
    loadView(element, "concertsView");
}

function doCreateUser(element){
    let u = document.getElementById("createUsername").value;
    let p = document.getElementById("createPassword").value;

    console.log("Username: " + u + "\nPassword: " + p);
    loadView(element, "loginView");
}

function doCreateConcert(element){
    let t = document.getElementById("createTitle").value;
    let d = document.getElementById("createDescription").value;
    let i = window.URL.createObjectURL(document.getElementById("createImage").files[0]);
    concerts.push({
        title: t,
        description: d,
        image: i
    });
    console.log(i);
    loadView(element, "concertsView");
}

function onHandleCreateUser(element){
    loadView(element, "createUserView");
}

function loadView(element, nextView){
    element.parentNode.style.display = "none";

    if(nextView ==="loginView"){
        document.getElementById("loginView").style.display = "block";
    }

    if(nextView ==="createUserView"){
        document.getElementById("createUserView").style.display = "block";
    }

    if(nextView ==="concertsView"){
        let el = document.getElementById("concertsView");
        loadConcerts(el);
        el.style.display = "block";
    }

    if(nextView ==="createConcertView"){
        document.getElementById("createConcertView").style.display = "block";
    }
}

function onHandleCreateConcert(element){
    loadView(element, "createConcertView");
}

function loadConcerts(element){

    while (element.firstChild) {
        element.removeChild(element.firstChild);
      }
    // do some fetch
    // let concerts;
    concerts.forEach((concert) => {
        console.log(concert);
        let title = document.createElement("p");
        title.innerHTML = concert.title;
        
        let desc = document.createElement("p");
        desc.innerHTML = concert.description;

        let image = document.createElement("image");
        image.src = concert.image;
        image.style.height = "250px";
        image.style.width = "250px";

        element.appendChild(image);
        element.appendChild(title);
        element.appendChild(desc);
    });

    let createConcertButton = document.createElement("button");
    createConcertButton.innerHTML = "create";
    createConcertButton.onclick = () => onHandleCreateConcert(element.firstChild);

    element.appendChild(createConcertButton);
}