
Vue.component("detalji-vm", {
	data: function () {
        return {
            id:"",
            vmKategorije : "",
            ime : "",
            kategorija : "",
            katIme:"",
            orgIme:"",
            virtuelnaMasina: "",
            tipKorisnika:"",
            diskovi:""
        }
    },
	methods : {
		checkParams: checkFormParams
        ,
        izmeni:function(){
            console.log(this.kategorija);
            console.log(this.ime);
            if(!this.checkParams()){
                return;
            }

            let promise = axios.put("/virtuelneMasine/"+this.virtuelnaMasina.id,{
                ime: this.ime,
                kategorija: this.kategorija
              }
            )
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
        reload:function name() {
            axios.get('/virtuelneMasine/'+this.id).then(response => {
                this.virtuelnaMasina = response.data;
                console.log(response.data);
                this.ime = response.data.ime;
                this.katIme = response.data.kategorija.ime;
                this.orgIme = response.data.organizacija.ime;
            }).catch(error=> {
                let msg = error.response.data.ErrorMessage;
                new Toast({
                    message: msg,
                    type: 'danger'
                });
            });
        },
        activnost:function () {
            let promise = axios.put("/virtuelneMasine/activnost/"+this.virtuelnaMasina.id,{}).then(res=>{
                this.$router.go();
            });
            promise.catch(error=>{
                new Toast({
                    message:error.response.data.ErrorMessage,
                    type: 'danger'
                });
            });
        },
        obrisi:function(){
            axios.delete('/virtuelneMasine/'+this.virtuelnaMasina.id).then(response => {
                    this.$router.push("/vm");
             }).catch(error=> {
                let msg = error.response.data.ErrorMessage;
                new Toast({
                    message: msg,
                    type: 'danger'
                });
            });
         },
         brisanjeAktivnosti:function(a){
            axios.delete("/virtuelneMasine/"+this.virtuelnaMasina.id+"/"+a.id).then(response =>{
                this.reload();
                new Toast({
                    message:"Uspesno obrisana aktivnost",
                    type: 'success'
                });
            }).catch(error => {
                let msg = error.response.data.ErrorMessage;
                new Toast({
                    message: msg,
                    type: 'danger'
                });
            });
        },
        izmenaAktivnosti:function(a){
            let pocetak = document.getElementById("poc"+a.id).value;
            let kraj = document.getElementById("kraj"+a.id).value;
            if(pocetak=="" && kraj=="" ){
                new Toast({
                    message:"Popuni pravilno makar jedno od polja za izmenu,",
                    type: 'danger'
                });
            }else {
                novaAktivnost = {
                    pocetak: pocetak,
                    zavrsetak: kraj
                };
                axios.put("/virtuelneMasine/" + this.virtuelnaMasina.id + "/" + a.id, novaAktivnost).then(response => {
                    this.reload();
                }).catch(error => {
                    let msg = error.response.data.ErrorMessage;
                    new Toast({
                        message: msg,
                        type: 'danger'
                    });
                });
            }
        },
        
        izmenaDiska:function (disk) { 
            let ime = document.getElementById("ime"+disk.id).value;
            let kapacitet = document.getElementById("kapacitet"+disk.id).value;
            axios.put("/diskovi",{id:disk.id,ime:ime,kapacitet:kapacitet}).then(response=>{
                new Toast({
                    message: "Uspešno je izmenjen disk!",
                    type: 'success'
                });
                this.reload();
            }).catch(error => {
                let msg = error.response.data.ErrorMessage;
                new Toast({
                    message: msg,
                    type: 'danger'
                });
            });
        },
        brisanjeDiska:function (disk) {
            axios.delete("/diskovi/"+disk.id).then(response=>{
                this.reload();
            }).catch(error => {
                let msg = error.response.data.ErrorMessage;
                new Toast({
                    message: msg,
                    type: 'danger'
                });
            });
        },
        back:function () {
            this.$router.go(-1);
        }
    },
	mounted () {
        this.id =  this.$route.params.vm;
        this.tipKorisnika = this.$route.params.tipKorisnika;
        if(this.tipKorisnika=="KORISNIK"){
            $("select input").prop("readonly", true);
        }
        axios.get('/vmKategorije').then(response => {
            this.vmKategorije = response.data;
        }).catch(error=>{
            new Toast({
                message:error.response.data.ErrorMessage,
                type: 'danger'
            });
        });
        this.reload();
    },
    template: ` 
<div>
    <v-row>
        <v-col col="10">
            <h3>Detalji virtuelne mašine</h3>
        </v-col>
        <v-col col="2">
            <v-container class="d-flex flex-row-reverse">
                <v-btn color="primary" @click="back">Nazad</button>
            <v-container>
        </v-col>
    </v-row>
    <v-row>
        <v-col cols="12" lg="4">
            <v-card> 
                <v-container>   
                    <h3>Virtuelna mašina: {{ime}}</h3>
                    <v-simple-table>
                        <tbody>
                            <tr>
                                <td>
                                    Ime virtuelne masine:
                                </td>
                                <td>
                                    <input class="required" type="text" v-model="ime" v-bind:placeholder="katIme"/>
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
                                    {{katIme}}
                                </td>
                                
                            </tr>
                            <tr>
                                <td>
                                    Nova kategorija:
                                </td>
                                <td>
                                    <select class="required" v-model="kategorija">
                                        <option  v-if="vmKategorije" :selected="kat==kategorija" v-for = "kat in vmKategorije" v-bind:value="kat.ime">{{kat.ime}}</option>
                                    </select>
                                </td>
                                <td>
                                    <p  class="alert alert-danger d-none">
                                        Ovo polje je obavezno!
                                    </p>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Organizacija
                                </td>
                                <td>
                                    {{orgIme}}
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Aktivnost:
                                </td>
                                <td>
                                    <label class="switch">
                                        <input type="checkbox" v-bind:checked="virtuelnaMasina.isActiv" v-on:click="activnost()">
                                        <span class="slider round"></span>
                                    </label>
                                </td>
                            </tr>
                        </tbody>
                    </v-simple-table>
                    <button v-if="tipKorisnika!='KORISNIK'" class="btn btn-success"  @click = "izmeni()">Izmeni vm</button>
                    <button v-if="tipKorisnika!='KORISNIK'" class="btn btn-danger"  @click = "obrisi()">Obriši vm</button>
                <v-container>
            </v-card>
        </v-col>	
        <v-col cols="12" lg="8">
            <v-card>
                <v-container>
                    <h2>Pregled aktivnosti</h2>
                    <p v-if="virtuelnaMasina.aktivnosti.length==0">Trenutno nema aktivnosti</p>
                    <v-simple-table v-else>

                        <thead>
                            <th>
                                Pocetak aktivnosti
                            </th>
                            <th>
                                Kraj aktivnosti
                            </th>

                            <th v-if="tipKorisnika=='SUPER_ADMIN'">
                                Izmeni aktivnost
                            </th>
                            <th v-if="tipKorisnika=='SUPER_ADMIN'">
                                Obriši aktivnost
                            </th>
                        </thead>
                        <tbody>
                            <tr v-if="virtuelnaMasina&&virtuelnaMasina.aktivnosti" v-for = "a in virtuelnaMasina.aktivnosti" >
                                <td>
                                    {{a.pocetak}}
                                    <input v-if="tipKorisnika=='SUPER_ADMIN'" v-bind:id="'poc'+a.id" type="datetime-local" v-bind:value="a.pocetak">

                                </td>
                                <td text-align="center">
                                    {{a.zavrsetak}}
                                    <input v-if="tipKorisnika=='SUPER_ADMIN'" v-bind:id="'kraj'+a.id" type="datetime-local" v-bind:value="a.zavrsetak">
                                </td>
                                <td v-if="tipKorisnika=='SUPER_ADMIN'" text-align="center">
                                    <button type="button" class="btn btn-secondary" v-if="a.zavrsetak!=null" @click= "izmenaAktivnosti(a)" >Izmeni aktivnost</button>
                                </td>
                                <td v-if="tipKorisnika=='SUPER_ADMIN'" text-align="center">
                                    <button type="button" class="btn btn-secondary" v-if="a.zavrsetak!=null" @click= "brisanjeAktivnosti(a)" >Obrisi aktivnost</button>
                                </td>
                            </tr>
                        </tbody
                    </v-simple-table>
                <v-container>
            </v-card>
        </v-col>	
    </v-row>	
    <v-row>	
        <v-col cols="12">
            <v-card>
                <v-container>
                    <div class="page-header">
                        <h2>Pregled diskova</h2>
                    </div>
                    <p v-if="virtuelnaMasina.diskovi.length==0">Trenutno nema diskova</p>
                    <table class="table" v-else>
                        <tr>
                            <th>
                                Naziv diska
                            </th>
                            <th>
                                Kapacitet
                            </th>
                            <th v-if="tipKorisnika!='KORISNIK'">
                                Izmeni disk
                            </th>
                            <th v-if="tipKorisnika=='SUPER_ADMIN'">
                                Obriši disk
                            </th>
                        </tr>

                        <tr  v-if="virtuelnaMasina&&virtuelnaMasina.diskovi" v-for = "disk in virtuelnaMasina.diskovi" >
                            <td>
                                <input v-bind:id="'ime'+disk.id" type="text" v-bind:value="disk.ime">
                            </td>
                            <td text-align="center">
                                <input v-bind:id="'kapacitet'+disk.id" type="number" v-bind:value="disk.kapacitet">
                            </td>
                            <td v-if="tipKorisnika!='KORISNIK'">
                                <button type="button" class="btn btn-secondary" @click= "izmenaDiska(disk)" >Izmeni disk</button>
                            </td>
                            <td v-if="tipKorisnika=='SUPER_ADMIN'" text-align="center">
                                <button type="button" class="btn btn-secondary" @click= "brisanjeDiska(disk)" >Obriši disk</button>
                            </td>
                        </tr>
                    </v-simple-table>
                <v-container>
            </v-card>
        </v-col>
    <v-row>
</div>
`
	
});
