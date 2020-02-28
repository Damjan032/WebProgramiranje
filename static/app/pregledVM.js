Vue.component("pregled-vm",{
    data:function () {
        return{
            virtuelneMasine : null,
           kategorijeBrJezgara : [],
           kategorijeRAM : [],
           kategorijeGPU : [],
           tipKorisnika : null,
           naziv : "",

           
           minRam:"",
           maxRam:"",

           minJZG:"",
           maxJZG:"",
           
           minGPU:"",
           maxGPU:"",

           rangeRAM:[],
           rangeJZG:[],
           rangeGPU:[],

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
<div class="container">

        <div class="page-header">
            <h2>Pregled virtuelnih mašina</h2>
        </div>       
        
        <p>
            <button class="btn btn-primary" type="button" data-toggle="collapse" data-target="#filtriraj" aria-expanded="false" aria-controls="multiCollapseExample2">Filtriraj</button>
        </p>
        <div class="collapse multi-collapse" id="filtriraj">
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
                            <template v-slot:prepend>
                            <v-text-field
                                disabled
                                :value="rangeRAM[0]"
                                class="mt-0 pt-0"
                                hide-details
                                single-line
                                type="number"
                                style="width: 60px"
                            ></v-text-field>
                            </template>
                            <template v-slot:append>
                            <v-text-field
                                disabled
                                :value="rangeRAM[1]"
                                class="mt-0 pt-0"
                                hide-details
                                single-line
                                type="number"
                                style="width: 60px"
                            ></v-text-field>
                            </template>
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
                            <template v-slot:prepend>
                            <v-text-field
                                disabled
                                :value="rangeJZG[0]"
                                class="mt-0 pt-0"
                                hide-details
                                single-line
                                type="number"
                                style="width: 60px"
                            ></v-text-field>
                            </template>
                            <template v-slot:append>
                            <v-text-field
                                disabled
                                :value="rangeJZG[1]"
                                class="mt-0 pt-0"
                                hide-details
                                single-line
                                type="number"
                                style="width: 60px"
                            ></v-text-field>
                            </template>
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
                            <template v-slot:prepend>
                            <v-text-field
                                disabled
                                :value="rangeGPU[0]"
                                class="mt-0 pt-0"
                                hide-details
                                single-line
                                type="number"
                                style="width: 60px"
                            ></v-text-field>
                            </template>
                            <template v-slot:append>
                            <v-text-field
                                disabled
                                :value="rangeGPU[1]"
                                class="mt-0 pt-0"
                                hide-details
                                single-line
                                type="number"
                                style="width: 60px"
                            ></v-text-field>
                            </template>
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
        </div>
        <table class="table">
            <tr>
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
            </tr>

            <tr v-for = "vm in virtuelneMasine" >
                <td text-align="center">
                    <router-link class = "block-link" :to="{name:'detaljiVM', params:{vm:vm.id, tipKorisnika:tipKorisnika}}">
                        <p>{{vm.ime}}</p>
                    </router-link>
                </td>
                <td text-align="center">
                    <router-link class = "block-link" :to="{name:'detaljiVM', params:{vm:vm.id, tipKorisnika:tipKorisnika}}">
                        {{vm.kategorija.brJezgra}}
                    </router-link>
                </td>
                <td text-align="center">
                    <router-link class = "block-link" :to="{name:'detaljiVM', params:{vm:vm.id, tipKorisnika:tipKorisnika}}">
                        {{vm.kategorija.RAM}}
                    </router-link>
                </td>
                <td text-align="center">
                    <router-link class = "block-link" :to="{name:'detaljiVM', params:{vm:vm.id, tipKorisnika:tipKorisnika}}">
                        {{vm.kategorija.brGPU}}
                    </router-link>
                </td>
                <td text-align="center">
                    <router-link class = "block-link" :to="{name:'detaljiVM', params:{vm:vm.id, tipKorisnika:tipKorisnika}}">
                        <p>{{vm.organizacija.ime}}</p>
                    </router-link>
                </td>
            </tr>
            <router-link v-if = "tipKorisnika!='KORISNIK'" to="/dodajVM">
                <button type="button" class="btn btn-success">Dodaj virtuelnu mašinu</button>
            </router-link>
        </table>
    </div>
    `
});