Vue.component("site-header", {
    props:['korisnik'],
	template: ` 
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <button v-if = "korisnik.uloga" class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbar" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <a href = "/" class="navbar-brand">Cloud service</a>
        <div  class="collapse navbar-collapse" id="navbar">
            <ul class="navbar-nav mr-auto">
                <li v-if = "korisnik.uloga" class="nav-item">
                    <router-link class="nav-link" to="/vm">Virtualne mašine</router-link>
                </li>
                <li v-if = "korisnik.uloga == 'SUPER_ADMIN'" class="nav-item">
                    <router-link class="nav-link" to="/kat">Kategorija</router-link>
                </li>
                <li v-if = "korisnik.uloga" class="nav-item">
                    <router-link class="nav-link" to="/disk">Diskovi</router-link>
                </li>
                <li class="nav-item" v-if = "korisnik.uloga=='SUPER_ADMIN'">
                    <router-link class="nav-link" to="/org">Organizacije</router-link>
                </li>
            
                <li class="nav-item">
                    <router-link class="nav-link" v-if = "korisnik.uloga=='SUPER_ADMIN'||korisnik.uloga=='ADMIN'" to="/korisnik">Korisnici</router-link>
                </li>

                

            </ul>
        </div>
        <span v-if = "korisnik.uloga" class="navbar-text">
            <router-link to = "/izmenaProfila"><button class = "dropdown-item">Izmeni nalog</button></router-link>
            <router-link v-if="korisnik.uloga=='ADMIN'" :to="{name:'detaljiOrg', params:{org:korisnik.organizacija, tipKorisnika:korisnik.uloga}}"><button class = "dropdown-item">Izmeni organizaciju</button></router-link>
            <a v-if="korisnik.uloga=='ADMIN'" href = "/racuni.html"><button class = "dropdown-item">Pregled računa</button></a>
            <button  class = "dropdown-item" v-on:click = "odjava()">Odjavi se</button>
        </span>
        
    </nav>	  
`
    , 
	methods : {
        odjava:function () {
            axios.get('/logout').then(response => {
                if(response.data.status){
                    this.$router.push("/");
                    this.$emit('korisnik', "");

                    // window.location.replace("/login.html");
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
        // axios.get('/korisnik').then(response => {
        //     if(response.data){
        //         this.type = response.data.uloga;
        //         this.orgid =  response.data.organizacija;
        //     }
        // }); 
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