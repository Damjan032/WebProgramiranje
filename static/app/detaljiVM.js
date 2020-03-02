
Vue.component("detalji-vm", {
	data: function () {
        return {
            id:"",
            vmKategorije : [],
            diskovi:[],
            aktivnosti:[],
            ime : "",
            kategorija : "",
            katIme:"",
            orgIme:"",
            virtuelnaMasina: "",
            tipKorisnika:"",
            validdisk:false,
            rule:[v=>!!v||'Ovo polje je obavezno'],
            aktivnost:false,
            datetime:"",


            hovers:[]
        }
    },
    watch:{
        aktivnost:function (n) {
            console.log(n);
            
            this.activnost();
        }
    },
	methods : {
		checkParams: checkFormParams
        ,
        izmeni:function(){
            if(!this.$refs.formaVM.validate()){
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
                this.diskovi = response.data.diskovi;
                this.aktivnosti = response.data.aktivnosti;
                for(let a of aktivnosti){
                    this.hovers.push(true);
                }
                this.aktivnost = response.data.isActiv;
            }).catch(error=> {
                let msg = error.response.data.ErrorMessage;
                new Toast({
                    message: msg,
                    type: 'danger'
                });
            });
        },
        activnost:function () {
            let promise = axios.put("/virtuelneMasine/activnost/"+this.virtuelnaMasina.id).then(res=>{
                this.reload();
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
            let pocetak = a.pocetak;
            let kraj = a.zavrsetak;
            if(pocetak=="" && kraj=="" ){
                new Toast({
                    message:"Popuni pravilno makar jedno od polja za izmenu,",
                    type: 'danger'
                });
            }else {
                axios.put("/virtuelneMasine/" + this.virtuelnaMasina.id + "/" + a.id, a).then(response => {
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
            axios.put("/diskovi",disk).then(response=>{
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
        <v-col cols="10">
            <h3>Detalji virtuelne mašine</h3>
        </v-col>
        <v-col cols="2">
            <v-container class="d-flex flex-row-reverse">
                <v-btn color="primary" @click="back">Nazad</v-btn>
            </v-container>
        </v-col>
    </v-row>   

    <v-row>
        <v-col cols="12" lg="4">
            <v-card height="100%"s> 
                <v-container>   
                    <h3>Virtuelna mašina: {{ime}}</h3>
                    <v-form ref="formaVM">
                        <v-text-field
                            required
                            label="Ime virtuelne mašine"
                            v-model="ime"
                            :rules="rule"
                            :readonly="tipKorisnika=='KORISNIK'?true:false"

                        >
                        </v-text-field>
                        <v-divider class="my-3"></v-divider>

                        <p>
                            Kategorija: {{katIme}}
                        </p>
                        <v-divider class="my-3"></v-divider>
                        <v-select
                            v-if="tipKorisnika!='KORISNIK'"
                            required
                            :items="vmKategorije"
                            solo
                            item-text="ime"
                            item-value="id"
                            :rules="rule"
                            label="Nova kategorija"
                            v-model="kategorija"
                        >
                        </v-select>
                        <v-divider class="my-3"></v-divider>
                        <p>
                            Organizacija
                            {{orgIme}}
                        </p>
                        <v-divider class="my-3"></v-divider>
                        <p v-if="tipKorisnika!='KORISNIK'">
                            Aktivnost:    
                            <v-switch
                                v-model="aktivnost"
                            >
                            </v-switch>
                        </p>
                    </v-form>
                </v-container>
                <v-card-actions>
                    <v-btn v-if="tipKorisnika!='KORISNIK'" color="success"  @click = "izmeni()">Izmeni vm</v-btn>
                    <v-btn v-if="tipKorisnika!='KORISNIK'" color="error"  @click = "obrisi()">Obriši vm</v-btn>
               
                </v-card-actions>
            </v-card>
        </v-col>	
        <v-col cols="12" lg="8">
            <v-card height="100%" :class="{'subheading': $vuetify.breakpoint.xs}">
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
                            <tr v-if="aktivnosti" v-for = "(a, i) in aktivnosti" :key="a.id">
                                <td>
                                    <v-expansion-panels>
                                        <v-expansion-panel>
                                            <v-expansion-panel-header>
                                                {{a.pocetak}}
                                            </v-expansion-panel-header>
                                            <v-expansion-panel-content  v-if="tipKorisnika=='SUPER_ADMIN'">
                                                <v-date-time v-model="a.pocetak"/>
                                            </v-expansion-panel-content>
                                        </v-expansion-panel>
                                    </v-expansion-panels>
                                </td>
                                <td text-align="center">
                                    <v-expansion-panels v-if="a.zavrsetak!=null">
                                        <v-expansion-panel>
                                            <v-expansion-panel-header>
                                                {{a.zavrsetak}}
                                            </v-expansion-panel-header>
                                            <v-expansion-panel-content  v-if="tipKorisnika=='SUPER_ADMIN'">
                                                <v-date-time v-model="a.zavrsetak"/>
                                            </v-expansion-panel-content>
                                        </v-expansion-panel>
                                    </v-expansion-panels>
                                                                        
                                    <v-row align="center"
                                    justify="center" v-else>
                                        <v-icon>mdi-close</v-icon>
                                    </v-row>
                                    <p v-for="h in hovers">
                                        {{h}}
                                    </p>
                                </td>
                                <td v-if="tipKorisnika=='SUPER_ADMIN'" text-align="center">
                                    <v-btn x-small v-if="a.zavrsetak!=null" @click= "izmenaAktivnosti(a)" >Izmeni aktivnost</v-btn>
                                    <v-row align="center"
                                    justify="center" v-else>
                                        <v-icon>mdi-close</v-icon>
                                    </v-row>
                                </td>
                                <td v-if="tipKorisnika=='SUPER_ADMIN'" text-align="center">
                                    <v-btn x-small v-if="a.zavrsetak!=null" @click= "brisanjeAktivnosti(a)" >Obrisi aktivnost</v-btn>
                                    <v-row align="center"
                                    justify="center" v-else>
                                        <v-icon>mdi-close</v-icon>
                                    </v-row>
                                </td>
                            </tr>
                        </tbody>
                    </v-simple-table>
                </v-container>
            </v-card>
        </v-col>	
    </v-row>	
    <v-row>	
        <v-col cols="12">
            <v-card height="100%">
                <v-container>
                    <div class="page-header">
                        <h2>Pregled diskova</h2>
                    </div>
                    <p v-if="diskovi.length==0">Trenutno nema diskova</p>
                    <v-simple-table v-else>
                        <thead>
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
                        </thead>
                        <tbody>
                            <tr  v-if="virtuelnaMasina&&virtuelnaMasina.diskovi" v-for = "disk in diskovi" :key="disk.id">
                               
                                    <td>
                                        <v-text-field
                                            v-model="disk.ime"
                                            :rules="rule"
                                            :readonly="tipKorisnika=='KORISNIK'?true:false"
                                        >

                                        </v-text-field>
                                    </td>
                                    <td text-align="center">
                                        <v-text-field
                                            v-model="disk.kapacitet"
                                            :rules="rule"
                                            type="number"
                                            :readonly="tipKorisnika=='KORISNIK'?true:false"

                                        >

                                        </v-text-field>
                                    </td>
                                    <td v-if="tipKorisnika!='KORISNIK'">
                                        <v-btn x-small type="button" class="btn btn-secondary" @click= "izmenaDiska(disk)" >Izmeni disk</v-btn>
                                    </td>
                                    <td v-if="tipKorisnika=='SUPER_ADMIN'" text-align="center">
                                        <v-btn x-small  type="button" class="btn btn-secondary" @click= "brisanjeDiska(disk)" >Obriši disk</v-btn>
                                    </td>
                            </tr>
                        </tbody>
                    </v-simple-table>
                </v-container>
            </v-card>
        </v-col>
    </v-row>
</div>
`
	
});
