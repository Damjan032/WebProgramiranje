Vue.component("dodaj-vm",{
    data:function () {
        return{
            vmKategorije : "",
            ime : "",
            kategorija : "",
            org : null,
            organizacije:null,
            tipKorisnika:null
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
        }
    },
    template:`
<div  class="container">
    <div class="row">
        <div class="page-header col-8">
            <h2>Nova virtuelna mašina</h2>
        </div>
        <div>
            <router-link to="/">
                <button type="button" class="btn btn-primary">Nazad</button>
            </router-link>
        </div>
    </div>
    Ime virtuelne mašine: <input class="required" type="text" v-model="ime"/>
    <p  class="alert alert-danger d-none">
        Ovo polje je obavezno!
    </p>
    <br><br>
    Kategorija:
    <select class="required" v-model="kategorija" >
        <option v-for = "kat in vmKategorije" v-bind:value="kat.ime">{{kat.ime}}</option>
    </select>
    <p  class="alert alert-danger d-none">
        Ovo polje je obavezno!
    </p>
    <br><br>
    Organizacija:
    <select id="orginput" class="required" v-model="org" >
        <option v-for = "org in organizacije" v-bind:value="org.id">{{org.ime}}</option>
    </select>
    <p  class="alert alert-danger d-none">
        Ovo polje je obavezno!
    </p>
    <br><br>
    <button class="btn btn-secondary" @click = "addVM()">Dodaj kategoriju</button>
</div>
    `
});




