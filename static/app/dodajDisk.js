Vue.component("dodaj-disk",{
    data:function () {
        return{
            ime: null,
            tip: null,
            kapacitet: null,
            vm: null,
            org: null,
            vmasine: null,
            organizacije: null
        }
    },
    watch:{
        org:function (val) {
            if(val){
               let vau =  this.$refs.vm;
               console.log(vau);
               for(let o of this.organizacije){
                   console.log(o);
                   if(o.id === val){
                    if(o.resursi){
                        this.vmasine = [];
                        for(let r of o.resursi){
                            if(r.tipResursa=="VM"){
                                this.vmasine.push(r);
                            }
                        }
                    }

                    // break;
                   }
               }
            }
        }
    },
    mounted:function () {
        axios.get('/organizacije').then(response => {
            this.organizacije = response.data;
        }).catch(error=>{
            let msg = error.response.data.ErrorMessage;
            new Toast({
                message:msg,
                type: 'danger'
            });
        }); 
    },
    methods:{
        checkParams: checkFormParams
        ,
        dodajDisk:function() {
            if(!this.checkParams()){
                return;
            }
            let promise = axios.post("/diskovi",{
                ime: this.ime,
                tipDiska: this.tip,
                kapacitet: this.kapacitet,
                vm:this.vm,
                organizacija:this.org
              }
            )
            promise.then(response=>{
                    
                    if (response.status) {
                        this.$router.push("/disk");
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
        },
        back:function () {
            this.$router.go(-1);
        }
    },
    template:`
<div>

    <div class="row">
        <div class="page-header col-8">
            <h2>Novi disk</h2>
        </div>
        <div>
            <button type="button" class="btn btn-primary" @click="back">Nazad</button>
        </div>
    </div>
   
    <p>
        <table>
            <tr>
                <td>
                    Ime
                </td>
                <td>
                    <input class="required" type="text" v-model="ime"/>
                </td>
                <td >
                    <p  class="alert alert-danger d-none">
                        Ovo polje je obavezno!
                    </p>
                </td>
            </tr>
            
            <tr>
                <td>
                    Tip diska
                </td>
                <td>
                    
                    <select class="required" v-model="tip">
                        <option value="SSD">SSD</option>
                        <option value="HDD">HDD</option>
                    </select>
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
                <td>
                    <input class="required" type="number" min="1" v-model="kapacitet"/>
                </td>
                <td >
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
                    <select class="required" v-model="org">
                        <option v-for = "org in organizacije" :value="org.id">{{org.ime}}</option>
                    </select>
                </td>
                <td >
                    <p  class="alert alert-danger d-none">
                        Ovo polje je obavezno!
                    </p>
                </td>
            </tr>
            <tr>
                <td>
                    Virtuelna mašina
                </td>
                <td>
                    <select ref="vm" name="vm" v-model="vm">
                        <option v-for = "vm in vmasine" :value="vm.id">{{vm.ime}}</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>
                
                </td>
                <td>
                    <button type="button" class="btn btn-success" v-on:click = "dodajDisk()">Dodaj disk</button>
                </td>
            </tr> 
        </table>
    </p>
</div>
    `
});




