
Vue.component("test", {
    // props:{
    //     id:{
    //         type: Number,
    //         default:420
    //     }
    // },
	data: function () {
		    return {       		    
		    }
	},
    template: `
<div>
    {{$route.params.id}}
</div>	  
`
, 
	methods : {
		
	}
});