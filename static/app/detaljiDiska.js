
Vue.component("detalji-diska", {
	data: function () {
        return {
            id:null,
            ime: null,
            tip: null,
            kapacitet: null,
            vm:null,
            vmActiv:false,
            tipKorisnika:null
        }
    },
	methods : {
		checkParams: checkFormParams
        ,
        izmeniDisk:function() {
            console.log(this.korisnik);
            if(!this.checkParams()){
                return;
            }
            let promise = axios.put("/diskovi",{
                id:this.id,
                ime: this.ime,
                tip: this.tip,
                kapacitet: this.kapacitet,
                vm:this.vm
              }
            )
            promise.then(response=>{
                    
                    if (response.status == 200) {
                        window.location.replace("/diskovi.html");
                    }else{
                        new Toast({
                            message:response.statusText,
                            type: 'danger'
                        });
                    }

            });
            promise.catch(error=>{
                new Toast({
                    message:error.response.data.ErrorMessage,
                    type: 'danger'
                });
            })
        },
        obrisiDisk:function() {
            let promise = axios.delete("/diskovi/"+this.id);
            promise.then(response=>{
                    
                    if (response.status == 200) {
                        window.location.replace("/diskovi.html");
                    }else{
                        new Toast({
                            message:response.statusText,
                            type: 'danger'
                        });
                    }

            });
            promise.catch(error=>{
                new Toast({
                    message:error.response.data.ErrorMessage,
                    type: 'danger'
                });
            })
        },
        activnost:function (vm) {
            let promise = axios.put("/virtuelneMasine/activnost/"+vm,{});
            promise.catch(error=>{
                new Toast({
                    message:error.response.data.ErrorMessage,
                    type: 'danger'
                });
            });
        }
    },
	mounted () {
        this.ime =  this.$route.params.disk.ime;
        this.id = this.$route.params.disk.id;
        this.kapacitet =  this.$route.params.disk.kapacitet;
        this.tip = this.$route.params.disk.tip;
        this.vm = this.$route.params.disk.vm;
        let vm = this.vm;
        if(vm){
            if(vm.aktivnosti){
                if(vm.aktivnosti[vm.aktivnosti.length-1].zavrsetak==null){
                    this.vmActiv = true;
                }
            }
        }
        this.vmActiv;
        this.vmime = this.$route.params.disk.vmime;
        this.tipKorisnika = this.$route.params.tipKorisnika;
        if(this.tipKorisnika=="KORISNIK"){
            $("select input").prop("readonly", true);
        }
    },
    template: ` 
<div class="container">

    <h1>Disk: {{$route.params.disk.ime}}</h1>
    <table class="table">                
        <tr>
            <td>
                Ime 
            </td>
            <td>
                <input class="required" type="text" v-model = "ime">
            </td>
            <td >
                <p  class="alert alert-danger d-none">
                    Ovo polje je obavezno!
                </p>
            </td>
        </tr>
        <tr>
            <td>
                Kapacitet
            </td>
            <td colspan="2">
                <input  class="required" type="number" v-model = "kapacitet">
            </td>
            <td >
                <p  class="alert alert-danger d-none">
                    Ovo polje je obavezno!
                </p>
            </td>
        </tr>
        <tr>
            <td>
                Tip 
            </td>
            <td colspan="2">
                <select class="required" name="org" v-model="tip">
                    <option value="SSD">SSD</option>
                    <option value="HDD">HDD</option>
                </select> 
                <td >
                    <p  class="alert alert-danger d-none">
                        Ovo polje je obavezno!
                    </p>
                </td>
            </td>                           
        </tr>
        <tr v-if="vm">
            <td>
                Virtuelna mašina 
            </td>
            <td>
                {{vm.ime}}
            </td>
            <td>
                <label class="switch" v-if="vm">
                    <input type="checkbox" v-bind:checked="vmActiv" v-on:click="activnost(vm.id)">
                    <span class="slider round"></span>
                </label>
            </td>
        </tr>
        <tr>
            <td>
                <button v-on:click = "izmeniDisk()" type="button" class="btn btn-success">Izmeni disk</button>
                <button v-on:click = "obrisiDisk()" type="button" class="btn btn-danger">Obriši disk</button>
            </td>
        </tr>
    </table>    
</div>		  
`
	
});




