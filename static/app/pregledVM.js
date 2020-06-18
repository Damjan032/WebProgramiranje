Vue.component("pregled-vm",{
    data:function () {
        return{
            virtuelneMasine : [],
            kategorijeBrJezgara : [],
            kategorijeRAM : [],
            kategorijeGPU : [],
            tipKorisnika : "",
            naziv : "",


            minRam:"",
            maxRam:"",

            minJZG:"",
            maxJZG:"",

            minGPU:"",
            maxGPU:"",

            rangeRAM:[],
            rangeJZG:[],
            rangeGPU:[]
        }
    },
    
    mounted:function () {
       this.init();
    },
    methods:{
         init:function(){
                axios.get('/korisnik').then(response => {
                    if(response.data==null){
                        window.location.replace("/");
                    }
                    this.tipKorisnika = response.data.uloga;
                }).catch(error=>{
                    new Toast({
                        message:error.response.data.ErrorMessage,
                        type: 'danger'
                    });
                });
                axios.get('/virtuelneMasine').then(response => {
                    this.virtuelneMasine=response.data;
                    this.kategorijeBrJezgara = [];
                    this.kategorijeRAM = [];
                    this.kategorijeGPU = [];
                    for (let vm of this.virtuelneMasine) {
                        this.kategorijeBrJezgara.push(vm.kategorija.brJezgra);
                        this.kategorijeRAM.push(vm.kategorija.RAM);
                        this.kategorijeGPU.push(vm.kategorija.brGPU);
                    }
                    this.kategorijeGPU = [...new Set(this.kategorijeGPU)];
                    this.kategorijeBrJezgara = [...new Set(this.kategorijeBrJezgara)];
                    this.kategorijeRAM = [...new Set(this.kategorijeRAM)];
                   
                   
                    
                    //RAM
                    this.minRam =  Math.min.apply(Math,this.kategorijeRAM);
                    this.maxRam =  Math.max.apply(Math,this.kategorijeRAM);
                    this.rangeRAM = [this.minRam, this.maxRam];
                    // $( "#sliderRam" ).slider({
                    //     range: true,
                    //     min:minRam,
                    //     max: maxRam,            
                    // });
                    // $( "#sliderRam" ).on( "slidechange", function( event, ui ) {
                    //     var values = $( this ).slider( "option", "values" );
            
                    //     $("#ramOd").text(values[0]);
                    //     $("#ramDo").text(values[1]);
                    // } );
                    
                    // $( "#sliderRam" ).slider( "option", "max", maxRam);
                    // $( "#sliderRam" ).slider( "option", "min", minRam);
                    // $( "#sliderRam" ).slider( "option", "values",[minRam, maxRam ] );


                    //JEZGRA
                    this.minJZG =  Math.min.apply(Math,this.kategorijeBrJezgara);
                    this.maxJZG =  Math.max.apply(Math,this.kategorijeBrJezgara);
                    this.rangeJZG = [this.minJZG, this.maxJZG];
                    
                    // $( "#sliderJezgra" ).slider({
                    //     range: true,
                    //     min:minJezgra,
                    //     max: maxJezgra
                    // });
                    // $( "#sliderJezgra" ).on( "slidechange", function( event, ui ) {
                    //     var values2= $( "#sliderJezgra" ).slider( "option", "values" );
                    //     $("#jezgraOd").text(values2[0]);
                    //     $("#jezgraDo").text(values2[1]);
                    // } );
                    // $( "#sliderJezgra" ).slider( "option", "max", maxJezgra);
                    // $( "#sliderJezgra" ).slider( "option", "min", minJezgra);
                    // $( "#sliderJezgra" ).slider( "option", "values",[minJezgra, maxJezgra] );


                    //GPU
                    this.minGPU =  Math.min.apply(Math,this.kategorijeGPU);
                    this.maxGPU =  Math.max.apply(Math,this.kategorijeGPU);
                    this.rangeGPU = [this.minGPU, this.maxGPU];
                    
                    // $( "#sliderGpu" ).slider({
                    //     range: true,
                    //     min:minGPU,
                    //     max: maxGPU
                    // });
                    // $( "#sliderGpu" ).on( "slidechange", function( event, ui ) {
                    //     var values3= $( "#sliderGpu" ).slider( "option", "values" );
                    //     $("#gpuOd").text(values3[0]);
                    //     $("#gpuDo").text(values3[1]);
                    // } );
                    
                    // $( "#sliderGpu" ).slider( "option", "max", maxGPU);
                    // $( "#sliderGpu" ).slider( "option", "min", minGPU);
                    // $( "#sliderGpu" ).slider( "option", "values",[minGPU, maxGPU] );
                });
            },

            toDetaljiVM:function (vm) {
                this.$router.push({name:'detaljiVM', params:{vm:vm.id, tipKorisnika:this.tipKorisnika}});
            },
            toDodajVM:function () {
                this.$router.push("/dodajVM");
            },
            pregledAktivnosti:function(id){

                 this.$router.push("/aktinvosti/"+id);
            },
            activnost(vm){
             let promise = axios.put("/virtuelneMasine/activnost/"+vm.id,{});

            },

            filter(event) {
                    console.log(event.target.value);
                    console.log(this.filterIme);
            },
            obrisi:function(id){
               axios.delete('/virtuelneMasine/'+id).then(response => {
                       axios.get('/korisnik').then(response => {
                           this.virtuelnaMasina=response.data;
                       });
                }).catch(error=> {
                   let msg = error.response.data.ErrorMessage;
                   new Toast({
                       message: msg,
                       type: 'danger'
                   });
               });
            },

            pregledDiskova:function(id){
                window.location.href="/virtuelneMasineDiskovi.html?id="+id;
            },

            izmeni:function(id){
                window.location.href="/virtuelnaMasinaIzmena.html?id="+id;
            },
            filtriraj:function () {
                // var valuesRam = $( "#sliderRam" ).slider( "option", "values" );
                // var valuesJezgra = $( "#sliderJezgra" ).slider( "option", "values" );
                // var valuesGPU = $( "#sliderGpu" ).slider( "option", "values" );
                console.log(this.naziv);
                let pomocniNaziv = this.naziv;
                if(this.naziv.trim() == ""){
                    pomocniNaziv=" ";
                }
                axios.get("/virtuelneMasine/filtriraj/"+pomocniNaziv+"/"+this.rangeRAM[0]+"/"+this.rangeRAM[1]+"/"+this.rangeGPU[0]+"/"+this.rangeGPU[1]+"/"+this.rangeJZG[0]+"/"+this.rangeJZG[1]).then(response =>{
                    this.virtuelneMasine=response.data;
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
<div>
    <v-card>
    <v-container>

        <h2>Pregled virtuelnih mašina</h2>
        <h4 class="error my-10" v-if="virtuelneMasine&&virtuelneMasine.length==0">
        Trenutno nema virtuelnih mašina za pregled.
        </h4>
        <template v-else>
            <v-expansion-panels>

                <v-expansion-panel>
                    <v-expansion-panel-header>Filtriraj</v-expansion-panel-header>
                    <v-expansion-panel-content>
                        <table  class="table table-striped table-dark">
                            <tr>
                                <td align="center" colspan="5"><b>Filtriranje</b></td>
                            </tr>
                            <tr>
                                <td align="center" colspan="3">FILTRIRANJE PO</td>
                                <td>OD</td>
                                <td>DO</td>
                            </tr>
                            <tr>
                                <td align="center" colspan="3">Kolicini rama</td>
                                <td v-for="(r, i) in rangeRAM" :key="i"><p>{{r}}</p></td>
                            </tr>
                            <tr>
                                <td colspan="5">
                                    <v-range-slider
                                        v-model="rangeRAM"
                                        :max="maxRam"
                                        :min="minRam"
                                        hide-details
                                        class="align-center"
                                    >
                                    </v-range-slider>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" colspan="3">Broju jezgara</td>
                                <td v-for="(r, i) in rangeJZG" :key="i"><p>{{r}}</p></td>
                            </tr>
                            <tr>
                                <td colspan="5">
                                    <v-range-slider
                                        v-model="rangeJZG"
                                        :max="maxJZG"
                                        :min="minJZG"
                                        hide-details
                                        class="align-center"
                                    >
                                    </v-range-slider>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" colspan="3">Broju GPU jezgara</td>
                                <td v-for="(r, i) in rangeGPU" :key="i"><p>{{r}}</p></td>
                            </tr>
                            <tr>
                                <td colspan="5">
                                    <v-range-slider
                                        v-model="rangeGPU"
                                        :max="maxGPU"
                                        :min="minGPU"
                                        hide-details
                                        class="align-center"
                                    >
                                    </v-range-slider>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" colspan="3"><p>Nazivu</p></td>
                                <td  align="center" colspan="3">
                                    <input v-model="naziv" class="form-control" type="text" placeholder="Default input">
                                </td>
                            </tr>
                            <tr>
                                <td colspan="5">
                                    <button type="button"   class="btn btn-secondary btn-lg btn-block" @click="filtriraj()">Filtriraj</button>
                                </td>
                            </tr>
                        </table>   
                    </v-expansion-panel-content>
                </v-expansion-panel>
            </v-expansion-panels>

        

            <v-simple-table>
                <template v-slot:default>
                    <thead>
                        <th>
                            Naziv
                        </th>
                        <th>
                            Broj jezgara
                        </th>
                        <th>
                            RAM
                        </th>
                        <th>
                            GPU jezgra
                        </th>
                        <th>
                            Organizacija
                        </th>
                    </thead>
                    <tbody>
                        <tr :key="vm.id" v-for = "vm in virtuelneMasine" @click="toDetaljiVM(vm)">
                            <td text-align="center">
                                    <p>{{vm.ime}}</p>
                            </td>
                            <td text-align="center">
                                    {{vm.kategorija.brJezgra}}
                            </td>
                            <td text-align="center">
                                    {{vm.kategorija.RAM}}
                            </td>
                            <td text-align="center">
                                    {{vm.kategorija.brGPU}}
                            </td>
                            <td text-align="center">
                                    <p>{{vm.organizacija.ime}}</p>
                            </td>
                        </tr>
                    </tbody>
                </template>
            </v-simple-table>
            </template>
        <router-link color="success" v-if = "tipKorisnika!='KORISNIK'" to="/dodajVM">
            <v-btn color="success">Dodaj virtuelnu mašinu</v-btn>
        </router-link>
        </v-container>

        </v-card>
    </div>
    `
});