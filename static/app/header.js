Vue.component("site-header", {
	data: function () {
		    return {
              type: "",
              orgid:null
		    }
	},
	template: ` 
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <button v-if = "type" class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbar" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <a href = "/" class="navbar-brand">Cloud service</a>
        <div  class="collapse navbar-collapse" id="navbar">
            <ul class="navbar-nav mr-auto">
                <li v-if = "type" class="nav-item">
                    <a class="nav-link" href="virtuelnaMasinaPregled.html">Virtualne mašine <span class="sr-only">(current)</span></a>
                </li>
                <li v-if = "type == 'SUPER_ADMIN'" class="nav-item">
                    <a class="nav-link" href="vmKatPregled.html">Kategorije v-mašina<span class="sr-only">(current)</span></a>
                </li>
                <li v-if = "type" class="nav-item">
                    <a class="nav-link" href="diskovi.html">Diskovi <span class="sr-only">(current)</span></a>
                </li>
                <li class="nav-item" v-if = "type=='SUPER_ADMIN'">
                    <a class="nav-link" href="organizacije.html">Organizacije <span class="sr-only">(current)</span></a>
                </li>
            
                <li class="nav-item">
                    <a v-if = "type=='SUPER_ADMIN'||type=='ADMIN'" class="nav-link" href="korisnici.html">Korisnici <span class="sr-only">(current)</span></a>
                </li>

                

            </ul>
        </div>
        <span v-if = "type" class="navbar-text">
            <a href = "/izmenaProfila.html"><button class = "dropdown-item">Izmeni nalog</button></a>
            <a v-if="type=='ADMIN'" v-bind:href = "'/orgIzmena.html?id='+orgid"><button class = "dropdown-item">Izmeni organizaciju</button></a>
            <a v-if="type=='ADMIN'" href = "/racuni.html"><button class = "dropdown-item">Pregled računa</button></a>
            <button  class = "dropdown-item" v-on:click = "odjava()">Odjavi se</button>
        </span>
        
    </nav>	  
`
	, 
	methods : {
        odjava:function () {
            axios.get('/logout').then(response => {
                if(response.data.status){
                    window.location.replace("/login.html");
                }else{
                    new Toast({
                        message:response.data.poruka,
                        type: 'danger'
                    });
                }
            })
        }
	},
	mounted () {
        axios.get('/korisnik').then(response => {
            this.type = response.data.uloga;
            this.orgid =  response.data.organizacija;
        }); 
    }
});



// Vue.component("header", function (resolve, reject) {
//     vue.$http.get("header.html", function (data, status, request) {
//        var parser = new DOMParser();
//        var doc = parser.parseFromString(data, "text/html");
//        resolve({
//            template: doc
//        }); 
//     });
// });


// let headerapp = new Vue({
//     el:"#header",
//     data: {
//         loggedin : false
//     },
//     mounted () {
//         axios.get('/isloggedin').then(response => {
//             loggedin = response.data;})    
//     }
// });