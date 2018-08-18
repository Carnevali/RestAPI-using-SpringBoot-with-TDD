<div class="generic-containers" ng-if="vm.isReady">
    <div class="panel panel-default">
        <div class="panel-heading col-sm-12" ng-repeat-start="c in vm.containers track by c.id">
            <h5>
                {{c.description}} - Temperature: {{c.temperature | number:2}}

                <div class="pull-right">
                    <span class="label {{c.status.label}}">{{c.status.value}}</span>
                </div>
            </h5>
        </div>
        <div class="panel-body" ng-repeat-end>
            <div class="formcontainter">
                <form name="vm.containerForm" class="form-horizontal no-padding-right no-padding-left">
                    <div ng-repeat="b in c.beers track by b.id" class="col-sm-2" style="padding: 0;">
                        <div class="row">
                            <div class="col-sm-12">
                                {{b.description}}
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-12">
                                Min: {{b.min}}   /   Max: {{b.max}}
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-12" ng-if="b.status.value == 'Ok'">
                                <span class="label label-success">All good mate</span>
                            </div>
                            <div class="col-sm-12" ng-if="b.status.value == 'Warning'">
                                <span class="label label-info" ng-if="b.min > c.temperature">The beer is getting freeze</span>
                                <span class="label label-danger" ng-if="b.max < c.temperature">The beer is getting hot</span>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>