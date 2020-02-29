
Vue.component("prijava",
{
    data:function () {
        return{    
            kime : "",
            sifra :"",
            valid:false,
            rule:[v=>!!v||'Ovo polje je obavezno'] 
        }
    },
    mounted:function() {        
        axios.get("/korisnik")
            .then(res=>{
                if(res.data){
                    this.$router.push("vm");
                    window.location.reload();
                }
            })
            .catch(error=>
                {
                    new Toast({
                        message:error.response.data.ErrorMessage,
                        type: 'danger'
                    });
                }
        );
    },
    methods:{
        login:function() {
            if(!this.$refs.login.validate()){
                 new Toast({
                     message:'Niste uneli sve podatke',
                     type: 'danger'
                 });
                 return;
            }
            let promise = axios.get("/login",{params: {
                kime:this.kime,
                sifra:this.sifra
              }
             }
            )
            promise.then(response=>{
                    if (response.data.status) {
                        this.$router.push("vm");
                        bus.$emit("korisnik", response.data.k);
                        // window.location.replace("/vm");
                    }else{
                        new Toast({
                            message:response.data.poruka,
                            type: 'danger'
                        });
                    }

            });
        }
    },
    template:
`
<div>
    <v-container>
        <h1>Prijava korisnika</h1>
    </v-container>
    <v-container>
        <v-card
            class="mx-auto"
        >
            <v-card-title>Prijavite se</v-card-title>     
            <v-container>   
                <v-form 
                    ref="login" 
                    v-model="valid"
                >
                    <v-text-field
                        v-model="kime"
                        label="Korisničko ime"
                        required
                        :rules="rule"
                        v-on:keyup.enter = "login"

                    >
                    </v-text-field>

                    <v-text-field
                        v-model="sifra"
                        label="Šifra"
                        type="password"
                        required
                        :rules="rule"
                        v-on:keyup.enter = "login"
                    >
                    </v-text-field>

                    <v-btn
                        color="success"
                        class="mr-4"
                        @click="login"
                    >
                        Prijavite se
                    </v-btn>
                </v-form>
            </v-container>
        </v-card>
    </v-container>

</div>    
    
`


});