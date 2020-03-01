Vue.component("dodaj-vm",{
    data:function () {
        return{
            vmKategorije : "",
            ime : "",
            kategorija : "",
            org : null,
            organizacije:null,
            tipKorisnika:null,
            valid:false,
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
        checkParams: checkFormParams
        ,
        addVM:function(){
            console.log(this.kategorija);
            if(!this.checkParams()){
                return;
            }

            let promise = axios.post("/virtuelneMasine",{
                    ime: this.ime,
                    kategorija: this.kategorija,
                    organizacija:this.org
            });
            promise.then(response=>{
                this.$router.push("/");
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
                    v-model="kategorija"
                    :items="vmKategorije"
                    label="Kategorija"
                    solo
                    item-text="ime"
                    item-value="id"
                >
                </v-select>

                <v-select
                    v-model="org"
                    :items="organizacije"
                    label="Organizacija"
                    solo
                    item-text="ime"
                    item-value="id"
                >
                </v-select>
            </v-form>
            <table class="table">
                <tr>
                    <td>
                        Ime virtuelne mašine: 
                    </td>
                        <input class="required" type="text" v-model="ime"/>
                    <td>
                    </td>
                    <td>
                        <p  class="alert alert-danger d-none">
                            Ovo polje je obavezno!
                        </p>
                    </td>
                </tr>
                <tr>
                    <td>
                        Kategorija:
                    </td>
                    <td>
                        <select class="required" v-model="kategorija" >
                            <option v-for = "kat in vmKategorije" v-bind:value="kat.ime">{{kat.ime}}</option>
                        </select>
                    </td>
                    <td>
                        <p  class="alert alert-danger d-none">
                            Ovo polje je obavezno!
                        </p>
                    </td>
                </tr>
                    <td>
                        Organizacija:
                    </td>
                    <td>
                        <select id="orginput" class="required" v-model="org" >
                            <option v-for = "org in organizacije" v-bind:value="org.id">{{org.ime}}</option>
                        </select>
                    </td>
                    <td>
                        <p  class="alert alert-danger d-none">
                            Ovo polje je obavezno!
                        </p>
                    </td>
                </tr>
            </table>
            <button class="btn btn-success" @click = "addVM()">Dodaj virtuelnu mašinu</button>
        </v-container>
    </v-card>
</div>
    `
});




