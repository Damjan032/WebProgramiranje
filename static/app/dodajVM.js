Vue.component("dodaj-vm",{
    data:function () {
        return{
            vmKategorije : [],
            organizacije:[],
            ime : "",
            kategorija : null,
            org : null,
            tipKorisnika:"",
            rule:[v=>!!v||'Ovo polje je obavezno']
        }
    },
    mounted:function () {
        axios.get('/vmKategorije').then(response => {
            this.vmKategorije = response.data;
            console.log(this.vmKategorije);
        }).catch(error=>{
            new Toast({
                message:error.response.data.ErrorMessage,
                type: 'danger'
            });
        });
        axios.get("/korisnik")
        .then(res=>{
            if(res.data){            
                this.tipKorisnika = res.data.uloga;
                    axios.get('/organizacije').then(response => {
                        this.organizacije = response.data;
                    }).catch(error=>{
                        new Toast({
                            message:error.response.data.ErrorMessage,
                            type: 'danger'
                        });
                    });
            }
        })
        .catch(error=>{
            new Toast({
                message:error.response.data.ErrorMessage,
                type: 'danger'
            })
        }); 
    },
    methods:{
        addVM:function(){
            if(!this.$refs.forma.validate()){
                return;
            }

            let promise = axios.post("/virtuelneMasine",{
                    ime: this.ime,
                    kategorija: this.kategorija,
                    organizacija:this.org
            });
            promise.then(response=>{
                this.$router.push("/vm");
            }).catch(error=>{
                let msg = error.response.data.ErrorMessage;
                new Toast({
                    message:msg,
                    type: 'danger'
                });
            });
        },
        back:function () {
            this.$router.go(-1);
        }
    },
    template:`
<div  class="container">
    <v-card>
        <v-container>
            <v-row>
                <v-col cols="8">
                    <h2>Nova virtuelna mašina</h2>
                </v-col>
                <v-col>
                    <router-link to="/">
                        <v-btn color="primary" @click="back">Nazad</v-btn>
                    </router-link>
                </v-col>
            </v-row>
            <v-form 
                ref="forma"
                v-model="valid"
            >
                <v-text-field
                    v-model="ime"
                    label="Ime virtuelne mašine"
                    required
                    :rules="rule"
                >
                </v-text-field>
                <v-select
                    required
                    v-model="kategorija"
                    :items="vmKategorije"
                    label="Kategorija"
                    solo
                    item-text="ime"
                    item-value="id"
                    :rules="rule"

                >
                </v-select>

                <v-select
                    required
                    v-model="org"
                    :items="organizacije"
                    label="Organizacija"
                    solo
                    item-text="ime"
                    item-value="id"
                    :rules="rule"

                >
                </v-select>
            </v-form>
            <button class="btn btn-success" @click = "addVM()">Dodaj virtuelnu mašinu</button>
        </v-container>
    </v-card>
</div>
    `
});




