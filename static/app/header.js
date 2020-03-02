Vue.component("site-header", {
    data:function name(params) {
      return{
          drawer:false
      }  
    },
    props:['korisnik'],
    template: ` 
<div>
    <v-app-bar
      absolute
      color="#6A76AB"
      dark
      prominent
      src="../img/cloud.jpeg"
      fade-img-on-scroll
    >
      <template v-slot:img="{ props }">
        <v-img
          v-bind="props"
          gradient="to top right, rgba(100,115,201,.7), rgba(25,32,72,.7)"
        ></v-img>
      </template>
      
      <v-app-bar-nav-icon @click="drawer = true" v-if="korisnik.uloga" 
      >
      </v-app-bar-nav-icon>

      <v-toolbar-title>       
        <router-link to = "/" tag="p" class="navbar-brand"><h1>Cloud service</h1></router-link>
      </v-toolbar-title>
      

      <template v-slot:extension>
        <transition name="slide-fade">
            <v-tabs align-with-title v-if = "korisnik.uloga"
            >
                <router-link tag="v-tab" class="nav-link" to="/vm">Virtualne mašine</router-link>
                <router-link tag="v-tab" class="nav-link" v-if = "korisnik.uloga=='SUPER_ADMIN'" to="/kat">Kategorija</router-link>
                <router-link tag="v-tab" class="nav-link" to="/disk">Diskovi</router-link>
                <router-link tag="v-tab" class="nav-link" to="/org">Organizacije</router-link>
                <router-link tag="v-tab" class="nav-link" v-if = "korisnik.uloga=='SUPER_ADMIN'||korisnik.uloga=='ADMIN'" to="/korisnik">Korisnici</router-link>
            </v-tabs>
        </transition>
      </template>
    </v-app-bar>

    <v-navigation-drawer
        v-model="drawer"
        absolute
        temporary
    >
        <v-list
        nav
        dense
        >
            <v-list-item-group>
                <v-list-item
                v-if="korisnik.uloga" 

                >
                    <v-list-item-title>                    
                        <router-link to = "/izmenaProfila"><button class = "dropdown-item">Izmeni nalog</button></router-link>
                    </v-list-item-title>
                </v-list-item>
                <v-list-item
                v-if="korisnik.uloga=='ADMIN'" 
                >
                    <v-list-item-title>                    
                        <router-link :to="{name:'detaljiOrg', params:{org:korisnik.organizacija, tipKorisnika:korisnik.uloga}}"><button class = "dropdown-item">Izmeni organizaciju</button></router-link>
                    </v-list-item-title>
                </v-list-item>
                <v-list-item
                v-if="korisnik.uloga=='ADMIN'" 
                >
                    <v-list-item-title>                    
                        <router-link to = "/racuni"><button class = "dropdown-item">Pregled računa</button></router-link>
                    </v-list-item-title>
                </v-list-item>
                <v-list-item
                v-if="korisnik.uloga" 

                >
                    <v-list-item-title>                    
                        <button  class = "dropdown-item" v-on:click = "odjava()">Odjavi se</button>
                    </v-list-item-title>
                </v-list-item>
            </v-list-item-group>
        </v-list>
    </v-navigation-drawer>
</div>
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