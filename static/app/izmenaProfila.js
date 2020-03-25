Vue.component("izmena-profila",{
    data:function name(params) {
        return{
            korisnik : null,
            email:null,
            ime:null,
            prezime:null,
            sifra1:null,
            sifra2:null,
            rules:[v=>!!v||'Ovo polje je obavezno'],
            erules:[v=>!!v||'Ovo polje je obavezno', e=>e.match(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)?!!e:"Email ne odgovara obrascu korisnik@kompanija.domen!"],
            
        }
    },
    mounted(){
        axios.get("/korisnik").then((res)=>{
            this.korisnik = res.data;
            this.email = this.korisnik.email;
            this.ime = this.korisnik.ime;
            this.prezime = this.korisnik.prezime;
        }).catch(error=>{
            new Toast({
                message:error.response.data.ErrorMessage,
                type: 'danger'
            });
        });
    },
    methods:{
        izmeniProfil:function () {

            if(!this.$refs.forma.validate())
                return
            if(this.sifra1&&!this.sifra2){
                new Toast({
                    message:"Niste ponovili šifru",
                    type: 'danger'
                });
            }else if(!this.sifra1&&this.sifra2){
                new Toast({
                    message:"Niste uneli šifru u prvo polje",
                    type: 'danger'
                });
            }else if(this.sifra1&&this.sifra1&&(this.sifra1!==this.sifra2)){
                new Toast({
                    message:"Šifre se ne slažu",
                    type: 'danger'
                });
            }else{
                axios.put("/korisnici",{
                    email: this.email,
                    ime: this.ime,
                    prezime: this.prezime,
                    sifra:this.sifra1
                    }
                ).then(response=>{   
                    if (response.status == 200) {
                        new Toast({
                            message:response.statusText,
                            type: 'success'
                        });
                    }else{
                        new Toast({
                            message:response.statusText,
                            type: 'danger'
                        });
                    }
                }).catch(error=>{
                    new Toast({
                        message:error.response.data.ErrorMessage,
                        type: 'danger'
                    });
                });
            }
       }
    },
    template:
`
<div>
    <v-container>

        <v-row>
            <h1>Izmena Profila</h1>
        </v-row>
        

        <v-card> 
            <v-container>
                <h2>Korisnik: {{email}}</h2>
                <v-form ref = "forma">
                    <v-text-field
                        label="Email"
                        v-model="email"
                    />
                    <v-text-field
                        label="Ime"
                        v-model="ime"
                    />
                    <v-text-field
                        :rules="rules"
                        required
                        label="Prezime"
                        v-model="prezime"
                    />
                    <v-text-field
                        v-model="sifra"
                        label="Šifra"
                        type="password"
                    />
                    <v-text-field
                        label="Ponovite šifru"
                        type="password"
                    />
                </v-form>
                <v-btn v-on:click = "izmeniProfil()" color="success">Izmeni profil</v-btn>
            </v-container>
        </v-card>    
    </v-container>

</div>
`
});


