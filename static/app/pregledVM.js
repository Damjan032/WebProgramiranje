Vue.component("vm",{
    data:function () {
        return{
            virtuelneMasine : null,
           kategorijeBrJezgara : [],
           kategorijeRAM : [],
           kategorijeGPU : [],
           korisnikType : null,
           naziv : ""
        }
    },
    mounted:function () {
       this.init();
    },
    methods:{
         init:function(){
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
                    let minRam =  Math.min.apply(Math,this.kategorijeRAM);
                    let maxRam =  Math.max.apply(Math,this.kategorijeRAM);
                    $( "#sliderRam" ).slider( "option", "max", maxRam);
                    $( "#sliderRam" ).slider( "option", "min", minRam);
                    $( "#sliderRam" ).slider( "option", "values",[minRam, maxRam ] );

                    let minJezgra =  Math.min.apply(Math,this.kategorijeBrJezgara);
                    let maxJezgra =  Math.max.apply(Math,this.kategorijeBrJezgara);
                    $( "#sliderJezgra" ).slider( "option", "max", maxJezgra);
                    $( "#sliderJezgra" ).slider( "option", "min", minJezgra);
                    $( "#sliderJezgra" ).slider( "option", "values",[minJezgra, maxJezgra] );

                    let minGPU =  Math.min.apply(Math,this.kategorijeGPU);
                    let maxGPU =  Math.max.apply(Math,this.kategorijeGPU);
                    $( "#sliderGpu" ).slider( "option", "max", maxGPU);
                    $( "#sliderGpu" ).slider( "option", "min", minGPU);
                    $( "#sliderGpu" ).slider( "option", "values",[minGPU, maxGPU] );
                });
            },

            pregledAktivnosti:function(id){

                 window.location.href="/virtuelneMasineAktinvosti.html?id="+id;
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
                var valuesRam = $( "#sliderRam" ).slider( "option", "values" );
                var valuesJezgra = $( "#sliderJezgra" ).slider( "option", "values" );
                var valuesGPU = $( "#sliderGpu" ).slider( "option", "values" );
                console.log(this.naziv);
                let pomocniNaziv = this.naziv;
                if(this.naziv.trim() == ""){
                    pomocniNaziv=" ";
                }
                axios.get("/virtuelneMasine/filtriraj/"+pomocniNaziv+"/"+valuesRam[0]+"/"+valuesRam[1]+"/"+valuesGPU[0]+"/"+valuesGPU[1]+"/"+valuesJezgra[0]+"/"+valuesJezgra[1]).then(response =>{
                    this.virtuelneMasine=response.data;
                }).catch(error=>{
                    let msg = error.response.data.ErrorMessage;
                    new Toast({
                        message:msg,
                        type: 'danger'
                    });
                });
                /*{
                    naziv : this.naziv,
                        ramOd : valuesRam[0],
                    ramDo : valuesRam[1],
                    jezgraOd : valuesJezgra[0],
                    jezgraDo : valuesJezgra[1],
                    gpuOd : valuesGPU[0],
                    gpuDo : valuesGPU[1]
                }*/
            }
    },
    template:`
<div class="jumbotron"><h1>Pregled virtuelnih mašina</h1></div>
    <div class="container">
        <div class="page-header">
            <h2>Virtuelne masine</h2>
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
                    <td><p id="ramOd"></p></td>
                    <td><p id="ramDo"></p></td>
                </tr>
                <tr>
                    <td colspan="5">
                        <div id="sliderRam"></div>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="3">Broju jezgara</td>
                    <td><p id="jezgraOd"></p></td>
                    <td><p id="jezgraDo"></p></td>
                </tr>
                <tr>
                    <td colspan="5">
                        <div id="sliderJezgra"></div>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="3">Broju GPU jezgara</td>
                    <td><p id="gpuOd"></p></td>
                    <td><p id="gpuDo"></p></td>
                </tr>
                <tr>
                    <td colspan="5">
                        <div id="sliderGpu"></div>
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
                    Kategorija
                </th>
                <th>
                    Pregled diskova
                </th>
                <th>
                    Pregled aktivnosti
                </th>
                <th>
                    Izmena
                </th>
                <th>
                    Brisanje
                </th>
                <th>
                    Ukljucen
                </th>
            </tr>

            <tr v-for = "vm in virtuelneMasine" >
                <td text-align="center">
                    <p>{{vm.ime}}</p>
                </td>
                <td text-align="center">
                    {{vm.kategorija.ime}}
                </td>
                <td>
                    <button type="button" @click= "pregledDiskova(vm.id)" class="btn btn-secondary">Pregled diskova</button>
                </td>
                 <td>
                    <button type="button" @click= "pregledAktivnosti(vm.id)" class="btn btn-secondary">Pregled aktivnosti</button>
                </td>
                <td>
                    <button type="button" class="btn btn-secondary" @click= "izmeni(vm.id)">Izmeni</button>
                </td>
                <td>
                    <button @click= "obrisi(vm.id)" type="button" class="btn btn-secondary">Obriši</button>
                </td>
                <td>
                    <label class="switch">
                        <!-- <input type="checkbox" v-model="module.checked" v-bind:id="module.id"> !-->
                        <input type="checkbox" v-bind:checked="vm.isActiv" v-on:click="activnost(vm)">
                        <span class="slider round"></span>
                    </label>
                </td>
            </tr>
            <tr>
                <td>
                    <a v-if = "korisnikType!='KORISNIK'" href="virtuelnaMasinaAdd.html">
                        <button type="button" class="btn btn-success">Dodaj virtuelnu mašinu</button>
                    </a>
                </td>
            </tr>
        </table>
    </div>


</div>

<div id="main">
    <br>
    <div id="slider"></div>
    <br>
</div>
<div id="footer">
<!-- <p id="sliderValsliderVal" v-model="vrednostSlidera">EVE MEEEEE</p> -->
</div>
    `


});